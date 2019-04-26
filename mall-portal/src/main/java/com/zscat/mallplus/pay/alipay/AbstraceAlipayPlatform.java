package com.zscat.mallplus.pay.alipay;


import com.zscat.mallplus.pay.AbstractPayPlatform;
import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.alipay.constant.AlipayConstant;
import com.zscat.mallplus.pay.alipay.util.AlipaySignUtil;
import com.zscat.mallplus.pay.constant.NotifyType;
import com.zscat.mallplus.pay.constant.PayConstant;
import com.zscat.mallplus.pay.exeception.BusinessException;
import com.zscat.mallplus.pay.util.NumberUtils;
import com.zscat.mallplus.pay.util.RequestUtil;
import com.zscat.mallplus.pay.util.XMLUtil;
import com.zscat.mallplus.pay.vo.PayNotify;
import com.zscat.mallplus.pay.vo.PayOrder.PayOrderType;
import com.zscat.mallplus.pay.vo.Refund;
import com.zscat.mallplus.pay.vo.RefundResult;
import com.zscat.mallplus.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstraceAlipayPlatform extends AbstractPayPlatform {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 解析支付成功通知
	 */
	@Override
	public PayNotify parsePayNotify(HttpServletRequest request, PayMethod paymethod, PayOrderType payOrderType) {
		Map<String, String> paramMap = RequestUtil.getParameterMap(request);
		log.info("[alipay]payNotify params:" + paramMap);
		String isSuccess = paramMap.get("is_success"); // 只有页面跳转通知时才有这个参数
		// null check 
		for (String key : AlipayConstant.PAYSUCCESS_NOTICE_NOTNULL_FIELD_LIST) {
			if (paramMap.get(key) == null) {
				//log.error("[alipay]invalid pay notice. " + key + " is null. " + paramMap);
				throw new BusinessException("[alipay]invalid pay notice. absence of necessary field[" + key	+ "]");
			}
		}
		if (StringUtils.isNotBlank(isSuccess) && !AlipayConstant.API_RET_SUCCESS.equals(isSuccess)) {
			throw new BusinessException("[alipay]payment not success.");
		}
		// sign check 
		String sign = paramMap.get("sign");
		String signType = paramMap.get("sign_type");
		if (!AlipaySignUtil.verifySign(sign, signType, paramMap, AlipayConstant.INPUT_CHARSET, paymethod)) {
			throw new BusinessException("[alipay]invalid pay notice sign." + paramMap);
		}
		
		// biz check
		if (!paramMap.get("notify_type").equals("trade_status_sync")) {
			throw new BusinessException("[alipay]invalid pay notice. invalid notify_type:" + paramMap);
		}
		String tradeStatus = paramMap.get("trade_status");
		if (!AlipayConstant.TRADE_SUCCESS.equals(tradeStatus) && !AlipayConstant.TRADE_FINISHD.equals(tradeStatus)) {
			throw new BusinessException("[alipay]invalid pay notice. invalid trade_status:" + paramMap);
		}
		// build ret
		PayNotify notify = new PayNotify();
		String payOrderId = paramMap.get("out_trade_no");
		notify.setPayOrderId(payOrderId);
		String orderIdStr = payOrderType == null ? payOrderId : payOrderId.replace(payOrderType.name(), "");
		notify.setOrderId(Long.parseLong(orderIdStr));
		notify.setPaymethod(paymethod);
		notify.setPaySeireId(paramMap.get("trade_no"));
		if (StringUtils.isBlank(isSuccess)) {
			notify.setPartnerPaysuccessTime(Timestamp.valueOf(paramMap.get("gmt_payment")));
		}
		notify.setTradeAmount(new BigDecimal(paramMap.get("total_fee")));
		notify.setParams(paramMap.get("extra_common_param"));
		if (StringUtils.isNotBlank(isSuccess)) {
			notify.setNotifyType(NotifyType.ONLINE);
		} else {
			notify.setNotifyType(NotifyType.BACK);
		}
		return notify;
	}

	/**
	 * 请求支付宝退款，支付宝的退款接口是批量的，但是网关每次只提交一笔
	 */
	@Override
	public RefundResult refund(Refund refund, PayMethod paymethod) {
		Map<String, String> map = new HashMap<>();
		map.put("service", AlipayConstant.REFUND_SERVICE_NAME);
		map.put("partner", paymethod.getPartnerId());
		map.put("_input_charset", AlipayConstant.INPUT_CHARSET);
		map.put("sign_type", AlipayConstant.REQUEST_SIGN_TYPE);
		map.put("notify_url", paymethod.getRefundNotifyUrl(PayOrderType.valueOf(refund.getPayOrderType())));
		map.put("batch_no", DateUtil.dateToStr(refund.getCreateTime(), "yyyyMMdd") + StringUtils.leftPad(""+refund.getRefundId(), 3, '0'));
		map.put("refund_date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		map.put("batch_num", "1");
		map.put("detail_data", refund.getPaySeireId() + "^" + NumberUtils.formatBigDecimal(refund.getRefundAmount(), "0.00") + "^退款");
		map.put("sign", AlipaySignUtil.sign(map, AlipayConstant.INPUT_CHARSET, AlipayConstant.REQUEST_SIGN_TYPE, paymethod));
		
		try
		{
			String paramStr = AlipaySignUtil.getParamStr(map, true, AlipayConstant.INPUT_CHARSET);
			
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet get = new HttpGet(AlipayConstant.ALIPAY_GATEWAY + paramStr);
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(5000).build();
			get.setConfig(config);
			log.info("[alipay]refund-request:" + paramStr);
			HttpResponse response = httpclient.execute(get);
			String responseStr = EntityUtils.toString(response.getEntity());
			log.info("[alipay]refund-response:" + responseStr);
			if (StringUtils.isBlank(responseStr)) {
				throw new BusinessException("[alipay]refund return empty. refundId=" + refund.getRefundId());
			}
			Map<String, String> retMap = XMLUtil.doXMLParse(responseStr);
			if (!AlipayConstant.API_RET_SUCCESS.equals(retMap.get("is_success"))) {
				throw new BusinessException("[alipay]request refund failed. refundId=" + refund.getRefundId() + ", error=" + retMap.get("error"));
			}
			RefundResult result = new RefundResult();
			result.setOrderId(refund.getOrderId());
			result.setRefundId(refund.getRefundId());
			result.setRefundAmount(refund.getRefundAmount());
			result.setRefundStatus(PayConstant.REFUND_STATUS_PROCESSED);
			if (refund.getPayOrderType() != null) {
				result.setPayOrderType(PayOrderType.valueOf(refund.getPayOrderType()));
			}
			
			return result;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException("[alipay]request refund error. refundId=" + refund.getRefundId(), e);
		}
	}

	/**
	 * 解析退款异步通知
	 */
	@Override
	public RefundResult parseRefundNotify(HttpServletRequest request, PayMethod paymethod, PayOrderType payOrderType) {
		Map<String, String> paramMap = RequestUtil.getParameterMap(request);
		// null check
		for (String key : AlipayConstant.REFUND_NOTICE_NOTNULL_FIELD_LIST) {
			if (paramMap.get(key) == null) {
				//log.error("[alipay]invalid refund notice. " + key + " is null. " + paramMap);
				throw new BusinessException("[alipay]invalid refund notice. absence of necessary field[" + key	+ "]");
			}
		}
		// sign check
		String sign = paramMap.get("sign");
		String signType = paramMap.get("sign_type");
		if (!AlipaySignUtil.verifySign(sign, signType, paramMap, AlipayConstant.INPUT_CHARSET, paymethod)) {
			throw new BusinessException("[alipay]invalid refund notify, verify sign failed.");
		}
		// biz check
		if (!paramMap.get("notify_type").equals("batch_refund_notify")) {
			throw new BusinessException("[alipay]invalid refund notice. invalid notify_type:" + paramMap);
		}
		RefundResult result = new RefundResult();
		result.setRefundId(Long.parseLong(paramMap.get("batch_no").substring(8)));
		int successNo = Integer.parseInt(paramMap.get("success_num"));
		// 虽然支付宝退款接口是批量的，但是网关这边都是单笔退款
		if (successNo == 0) {
			result.setRefundStatus(PayConstant.REFUND_STATUS_FAILED);
			log.error("[alipy]refund failed. " + paramMap);
		} else if (successNo == 1) {
			result.setRefundStatus(PayConstant.REFUND_STATUS_SUCC);
		} else {
			throw new BusinessException("[alipay]invalid refund notify, success_num bigger than 1 : " + successNo);
		}
		String detail = paramMap.get("result_details");
		String[] detailArr = detail.split("\\^");
		result.setPaySeireId(detailArr[0]);
//		result.setPartnerRefundId(paramMap.get("notify_id")); // 退款通知接口里面没有支付宝的退款ID，所以暂时用通知ID做退款ID
		result.setRefundAmount(new BigDecimal(detailArr[1]));
		if (payOrderType != null) {
			result.setPayOrderType(payOrderType);
		}
		return result;
	}
	
	@Override
	public String payNotifyResponse(boolean success) {
		if (success) {
			return "success";
		} else {
			return "failed";
		}
	}
}
