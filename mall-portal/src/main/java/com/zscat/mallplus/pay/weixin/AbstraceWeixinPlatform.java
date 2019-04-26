package com.zscat.mallplus.pay.weixin;


import com.zscat.mallplus.pay.AbstractPayPlatform;
import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.PayMethods;
import com.zscat.mallplus.pay.constant.NotifyType;
import com.zscat.mallplus.pay.constant.PayConstant;
import com.zscat.mallplus.pay.exeception.BusinessException;
import com.zscat.mallplus.pay.util.JacksonUtil;
import com.zscat.mallplus.pay.util.NumberUtils;
import com.zscat.mallplus.pay.util.RequestUtil;
import com.zscat.mallplus.pay.vo.PayNotify;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.vo.PayOrder.PayOrderType;
import com.zscat.mallplus.pay.vo.Refund;
import com.zscat.mallplus.pay.vo.RefundResult;
import com.zscat.mallplus.pay.weixin.constant.WeixinPayConstant;
import com.zscat.mallplus.pay.weixin.util.WeixinPayUtil;
import com.zscat.mallplus.util.DateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstraceWeixinPlatform extends AbstractPayPlatform {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected abstract String getTradeType();

	protected String getSceneInfo() {
		return "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"https://mall.rjmallplus.com\",\"wap_name\":\"Mall商城\"}}";
	}

	protected Map<String, String> prepay(PayOrder order, PayMethod paymethod) {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("appid", paymethod.getAppId());
			System.out.println("appid " + paymethod.getAppId());
			param.put("mch_id", paymethod.getPartnerId());
			System.out.println("mch_id " + paymethod.getPartnerId());
			param.put("nonce_str", WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
			System.out.println("nonce_str " + WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
			param.put("body", order.getGoodsName());
			System.out.println("body " + order.getGoodsName());
			param.put("out_trade_no", order.getPayOrderId());
			System.out.println("out_trade_no " + order.getPayOrderId());
			param.put("total_fee", NumberUtils.formatBigDecimal(order.getPayAmount().multiply(new BigDecimal("100")), "#"));
			System.out.println("total_fee " + NumberUtils.formatBigDecimal(order.getPayAmount().multiply(new BigDecimal("100")), "#"));
			param.put("spbill_create_ip", order.getUserIp());
			System.out.println("spbill_create_ip " + order.getUserIp());
			param.put("notify_url", paymethod.getPayNotifyUrl(order));
			System.out.println("notify_url " + paymethod.getPayNotifyUrl(order));
			String tradeType = getTradeType();
			param.put("trade_type", tradeType);
			System.out.println("trade_type " + tradeType);
			if (tradeType.equals(WeixinPayConstant.TRADE_TYPE_JSAPI)) {
				param.put("openid", order.getWeixinOpenId());
			}
			System.out.println("openid " + order.getWeixinOpenId());
			if (tradeType.equals(WeixinPayConstant.TRADE_TYPE_WAP)) {
				param.put("scene_info", getSceneInfo());
			}
			System.out.println("scene_info " + getSceneInfo());
			if (StringUtils.isNotBlank(order.getParams())) {
				param.put("attach", order.getParams());
			}
			System.out.println("attach " + order.getParams());
			String signStr = WeixinPayUtil.formatUnSignParaMap(param, false, false) + "&key=" + paymethod.getApiKey();
			System.out.println("signStr " + signStr);
			String sign = DigestUtils.md5Hex(signStr).toUpperCase();
			param.put("sign", sign);
			System.out.println("sign " + sign);

			String xml = JacksonUtil.obj2json(param).replace("HashMap", "xml");
			log.info("[weixinNew]requestToAddCharge-request: " + xml);
			System.out.println("xml " + xml);

			HttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(WeixinPayConstant.UNIFIED_ORDER_URL);
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(5000).build();
			post.setConfig(config);
			HttpEntity entity = new StringEntity(xml, "utf-8");
			post.setEntity(entity);

			int retryTimes = 1;
			boolean retry = true;
			while (retry) {
				retry = false;
				HttpResponse response = httpclient.execute(post);
				String retxml = EntityUtils.toString(response.getEntity(), "utf-8");
				log.info("[weixinNew]requestToAddCharge-response:" + retxml);
				if (StringUtils.isBlank(retxml)) {
					log.error("[weixinNew]prepay response is empty. orderId=" + order.getPayOrderId());
					throw new BusinessException("prepay response is empty. orderId=" + order.getPayOrderId());
				}
				@SuppressWarnings("unchecked")
				Map<String, String> retMap = JacksonUtil.json2pojo(retxml, HashMap.class);
				// 调用是否成功
				String retCode = retMap.get("return_code");
				if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(retCode)) {
					log.error("[weixinNew]prepay failed. orderId=" + order.getPayOrderId() + ", return_code=" + retCode + ", return_msg=" + retMap.get("return_msg"));
				    throw new BusinessException("prepay failed. orderId=" + order.getPayOrderId());
				}
				// 身份校验
				if (!paymethod.getPartnerId().equals(retMap.get("mch_id")) || !paymethod.getAppId().equals(retMap.get("appid"))) {
					log.error("[weixinNew]prepay error. invalid partner. paymethod=" + paymethod.name() + ", retxml=" + retxml);
					throw new BusinessException("prepay error. invalid partner.");
				}
				// 签名
				String retSign = retMap.remove("sign");
				signStr = WeixinPayUtil.formatUnSignParaMap(retMap, false, false) + "&key=" + paymethod.getApiKey();
				sign = DigestUtils.md5Hex(signStr).toUpperCase();
				if (!sign.equals(retSign)) {
					log.error("[weixinNew]prepay error. invalid response sign. response=" + retxml + ", sign=" + sign);
					throw new BusinessException("prepay error. invalid response sign");
				}

				// 交易结果校验
				String resultCode = retMap.get("result_code");
				if (WeixinPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
					// 预下单成功
					String prepayId = retMap.get("prepay_id");
				} else {
					String errCode = retMap.get("err_code");
					// 微信系统错误，需要重试
					if (WeixinPayConstant.SYSTEM_ERROR.equals(errCode)) {
						if (retryTimes < 3) {
							retryTimes++;
							retry = true;
							continue;
						} else {
							log.error("[weixinNew]partner system error when requestToAddCharge, and retry exceed maxTimes, retxml: " + retxml);
							throw new BusinessException("partner system error when requestToAddCharge, and retry exceed maxTimes, " + retMap.get("err_code_des"));
						}
					} else if (WeixinPayConstant.ERROR_PAID.equals(errCode)) {
						// 订单已经支付
						log.warn("already paid. orderId=" + order.getPayOrderId());
						throw new BusinessException("already paid. orderId=" + order.getPayOrderId());
					} else  {
						// 其他错误
						log.error("[weixinNew]prepay failed. orderId=" + order.getPayOrderId() + ", err_code=" + errCode + ", err_code_des=" + retMap.get("err_code_des"));
						throw new BusinessException("prepay failed. orderId=" + order.getPayOrderId() + ", err_code=" + errCode + ", err_code_des=" + retMap.get("err_code_des"));
					}
				}
				return retMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error when requestToAddCharge, " + e.getMessage(), e);
			throw new BusinessException("requestToAddCharge error. orderId=" + order.getPayOrderId(), e);
		}
		return null;
	}

	@Override
	public PayNotify parsePayNotify(HttpServletRequest request, PayMethod paymethod, PayOrderType payOrderType) {
		String xml = RequestUtil.getPostData(request);
		log.info("[weixinNew]payNotify : " + xml);
		if (StringUtils.isBlank(xml)) {
			log.error("[weixinNew]payNotify content is empty. paymethod=" + paymethod.name());
			throw new BusinessException("[weixinNew]payNotify content is empty. paymethod=" + paymethod.name());
		}
		@SuppressWarnings("unchecked")
		Map<String, String> map = JacksonUtil.json2maps(xml, HashMap.class);
		String returnCode = map.get("return_code").toString();
		if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
			log.error("[weixinNew]payNotify error. return failed. paymethod=" + paymethod.name() + ", return_code=" + returnCode + ", return_msg=" + map.get("return_msg") + ", xml=" + xml);
			throw new BusinessException("payNotify error. return failed. paymethod=" + paymethod.name() + ", return_code=" + returnCode + ", return_msg=" + map.get("return_msg") + ", xml=" + xml);
		}

		// 身份校验
		if (!paymethod.getPartnerId().equals(map.get("mch_id")) || !paymethod.getAppId().equals(map.get("appid"))) {
			log.error("[weixinNew]payNotify error. invalid partner. paymethod=" + paymethod.name() + ", xml=" + xml);
			throw new BusinessException("payNotify error. invalid partner. paymethod=" + paymethod.name());
		}
		// 签名校验
		String notifySign = map.remove("sign").toString();
		String signStr = WeixinPayUtil.formatUnSignParaMap(map, false, false) + "&key=" + paymethod.getApiKey();
		String sign = DigestUtils.md5Hex(signStr).toUpperCase();
		if (!sign.equals(notifySign)) {
			log.error("[weixinNew]invalid payNotify sign. paymethod=" + paymethod.name() + ", xml=" + xml + ", sign=" + sign);
			throw new BusinessException("invalid payNotify sign. paymethod=" + paymethod.name() + ", xml=" + xml + ", sign=" + sign);
		}

		// 业务结果
		String resultCode = map.get("result_code").toString();
		if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
			log.error("[weixinNew]payNotify error. paymethod=" + paymethod.name() + ", err_code=" + map.get("err_code") + ", err_code_des=" + map.get("err_code_des"));
			throw new BusinessException("payNotify error. paymethod=" + paymethod.name() + ", err_code=" + map.get("err_code") + ", err_code_des=" + map.get("err_code_des"));
		}
		PayNotify notify = new PayNotify();
		String payOrderId = map.get("out_trade_no").toString();
		notify.setPayOrderId(payOrderId);
		String orderIdStr = payOrderType == null ? payOrderId : payOrderId.replace(payOrderType.name(), "");
		notify.setOrderId(Long.parseLong(orderIdStr));
		notify.setPaymethod(paymethod);
		notify.setPaySeireId(map.get("transaction_id").toString());
		notify.setTradeAmount(new BigDecimal(map.get("total_fee")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN));
		notify.setPartnerPaysuccessTime(new Timestamp(DateUtil.strToDate(map.get("time_end").toString(), "yyyyMMddHHmmss").getTime()));
		notify.setNotifyType(NotifyType.BACK); // 微信只有后台通知
		notify.setParams(map.get("attach")); // 公共回传参数
		return notify;
	}

	@Override
	public RefundResult refund(Refund refund, PayMethod paymethod) {
		RefundResult result = new RefundResult();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", paymethod.getAppId());
			map.put("mch_id", paymethod.getPartnerId());
			map.put("nonce_str", WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
			String outTradeNo = StringUtils.isBlank(refund.getPayOrderType()) ? refund.getOrderId()+"" : (refund.getPayOrderType() + refund.getOrderId());
			map.put("out_trade_no",  outTradeNo);
			map.put("out_refund_no", "" + refund.getRefundId());
			map.put("total_fee", NumberUtils.formatBigDecimal(refund.getPayAmount().multiply(new BigDecimal("100")), "#"));
			map.put("refund_fee", NumberUtils.formatBigDecimal(refund.getPayAmount().multiply(new BigDecimal("100")), "#"));
			map.put("op_user_id", paymethod.getPartnerId());
			String signStr = WeixinPayUtil.formatUnSignParaMap(map, false, false) + "&key=" + paymethod.getApiKey();
			String sign = DigestUtils.md5Hex(signStr).toUpperCase();
			map.put("sign", sign);

			String xml = JacksonUtil.obj2json(map);
			log.info("[weixinNew]requestToRefund-request: " + xml);
			SSLContext sslContext = WeixinPayUtil.getSSLContext(paymethod);
			HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
			HttpPost post = new HttpPost(WeixinPayConstant.REFUND_URL);
			StringEntity entity = new StringEntity(xml);
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			String retxml = EntityUtils.toString(response.getEntity(), "utf-8");
			log.info("[weixinNew]requestToRefund-response : " + retxml);

			if (StringUtils.isBlank(retxml)) {
				log.error("[weixinNew]requestToRefund error. response is empty. refundId=" + refund.getRefundId());
				throw new BusinessException("requestToRefund error. response is empty. refundid=" + refund.getRefundId());
			}
			@SuppressWarnings("unchecked")
			Map<String, String> retMap = JacksonUtil.json2pojo(retxml, HashMap.class);
			String returnCode = retMap.get("return_code");
			if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				log.error("[weixinNew]requestToRefund failed. refundId=" + refund.getRefundId() + "return_code=" + returnCode + ", return_msg=" + retMap.get("return_msg"));
				throw new BusinessException("requestToRefund failed. refundId=" + refund.getRefundId() + "return_code=" + returnCode + ", return_msg=" + retMap.get("return_msg"));
			}
			// 身份校验
			if (!paymethod.getPartnerId().equals(retMap.get("mch_id")) || !paymethod.getAppId().equals(retMap.get("appid"))) {
				log.error("[weixinNew]requestToRefund error. invalid partner. paymethod=" + paymethod.name() + ", xml=" + retxml);
				throw new BusinessException("requestToRefund error. invalid partner. paymethod=" + paymethod.name());
			}
			// 签名校验
			String notifySign = retMap.remove("sign");
			signStr = WeixinPayUtil.formatUnSignParaMap(retMap, false, false) + "&key=" + paymethod.getApiKey();
			sign = DigestUtils.md5Hex(signStr).toUpperCase();
			if (!sign.equals(notifySign)) {
				log.error("[weixinNew]requestToRefund error. invalid response sign. paymethod=" + paymethod.name() + ", xml=" + retxml + ", sign=" + sign);
				throw new BusinessException("requestToRefund error. invalid response sign. paymethod=" + paymethod.name());
			}
			// 业务校验
			if(StringUtils.isNotBlank(refund.getPayOrderType()) && retMap.get("out_trade_no") != null) {
				retMap.put("out_trade_no", retMap.get("out_trade_no").replace(refund.getPayOrderType(), ""));
			}
			checkRefundResult(refund, retMap, paymethod);
			// 请求完成，并返回了结果
			result.setOrderId(Long.parseLong(retMap.get("out_trade_no")));
			result.setRefundId(refund.getRefundId());
			result.setRefundAmount(new BigDecimal(retMap.get("refund_fee")).divide(new BigDecimal(100)));
			result.setRefundStatus(PayConstant.REFUND_STATUS_PROCESSED);
			if (StringUtils.isNotBlank(refund.getPayOrderType())) {
				result.setPayOrderType(PayOrderType.valueOf(refund.getPayOrderType()));
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			// 请求抛出异常，合作方接口问题
			log.error("[Weixin] error when requestToRefund, " + e.getMessage(), e);
			throw new BusinessException(e.getMessage(), e);
		}

		return result;
	}

	private void checkRefundResult(Refund refund, Map<String, String> retMap, PayMethod paymethod) {
		String resultCode = retMap.get("result_code");
		if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
			throw new BusinessException("refund failed. refundId=" + refund.getRefundId() + ", err_code=" +  retMap.get("err_code") + ", err_code_des=" +  retMap.get("err_code_des"));
		}
		if (!String.valueOf(refund.getOrderId()).equals(retMap.get("out_trade_no"))) {
			throw new BusinessException("invalid ret out_trade_no " + retMap.get("out_trade_no") + "|" + refund.getOrderId());
		}
		if (!String.valueOf(refund.getRefundId()).equals(retMap.get("out_refund_no"))) {
			throw new BusinessException("invalid ret out_refund_no " + retMap.get("out_refund_no") + "|" + refund.getRefundId());
		}
		if (StringUtils.isBlank(retMap.get("transaction_id"))) {
			throw new BusinessException("invalid refund response, transaction_id is empty. refundId=" + refund.getRefundId());
		}
		if (StringUtils.isBlank(retMap.get("refund_id"))) {
			throw new BusinessException("invalid refund response. refund_id is empty. refundId=" + refund.getRefundId());
		}
		String refundFeeStr = NumberUtils.formatBigDecimal(refund.getRefundAmount().multiply(new BigDecimal("100")), "#");
		if (!refundFeeStr.equals(retMap.get("refund_fee"))) {
			throw new BusinessException("invalid ret refund_fee. refundId=" + refund.getRefundId() + ", " + retMap.get("refund_fee") + "|" + refundFeeStr);
		}
		String totalFeeStr = NumberUtils.formatBigDecimal(refund.getPayAmount().multiply(new BigDecimal("100")), "#");
		if (!totalFeeStr.equals(retMap.get("total_fee"))) {
			throw new BusinessException("invalid ret total_fee. refundId=" + refund.getRefundId() + ", " + retMap.get("total_fee") + "|" + totalFeeStr);
		}
	}

	@Override
	public RefundResult queryRefund(Refund refund) {
		RefundResult result = new RefundResult();
		try {
			PayMethods paymethod = PayMethods.valueOf(refund.getPaymethod());
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", paymethod.getAppId());
			map.put("mch_id", paymethod.getPartnerId());
			map.put("nonce_str", WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
			map.put("out_refund_no", "" + refund.getRefundId());
			String signStr = WeixinPayUtil.formatUnSignParaMap(map, false, false) + "&key=" + paymethod.getApiKey();
			String sign = DigestUtils.md5Hex(signStr).toUpperCase();
			map.put("sign", sign);

			String xml = JacksonUtil.obj2json(map);
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(WeixinPayConstant.UNIFIED_ORDER_URL);
			RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(5000).build();
			post.setConfig(config);
			HttpEntity entity = new StringEntity(xml, "utf-8");
			post.setEntity(entity);
			HttpResponse response = httpclient.execute(post);
			String retxml = EntityUtils.toString(response.getEntity());
			if (StringUtils.isBlank(retxml)) {
				throw new BusinessException("query refund error. response is empty. refundId=" + refund.getRefundId());
			}
			@SuppressWarnings("unchecked")
			Map<String, String> retMap = JacksonUtil.json2pojo(retxml, HashMap.class);
			String returnCode = retMap.get("return_code");
			if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(returnCode)) {
				throw new BusinessException("query charge failed. return_code=" + returnCode + ", return_msg=" + retMap.get("return_msg"));
			}
			// 身份校验
			if (!paymethod.getPartnerId().equals(retMap.get("mch_id")) || !paymethod.getAppId().equals(retMap.get("appid"))) {
				log.error("[weixinNew]refund failed. invalid partner. paymethod=" + paymethod.name() + ", xml=" + retxml);
				throw new BusinessException("[weixinNew]payNotify error. invalid partner. paymethod=" + paymethod.name());
			}
			// 签名校验
			String notifySign = retMap.remove("sign");
			signStr = WeixinPayUtil.formatUnSignParaMap(retMap, false, false) + "&key=" + paymethod.getApiKey();
			sign = DigestUtils.md5Hex(signStr).toUpperCase();
			if (!sign.equals(notifySign)) {
				throw new BusinessException("[weixinNew]invalid refund sign. paymethod=" + paymethod.name());
			}
			if(refund.getPayOrderType() != null) {
				retMap.put("out_trade_no", retMap.get("out_trade_no").replace(refund.getPayOrderType(), ""));
			}
			String resultCode = retMap.get("result_code");
			if (!WeixinPayConstant.RETURN_CODE_SUCCESS.equals(resultCode)) {
				throw new BusinessException("query refund failed. refundId=" + refund.getRefundId() + ", err_code=" +  retMap.get("err_code") + ", err_code_des=" +  retMap.get("err_code"));
			}
			if (!String.valueOf(refund.getOrderId()).equals(retMap.get("out_trade_no"))) {
				throw new BusinessException("invalid ret out_trade_no " + retMap.get("out_trade_no") + "|" + refund.getOrderId());
			}
			if (!String.valueOf(refund.getRefundId()).equals(retMap.get("out_refund_no_0"))) { // 如果用订单Id查询可能会有多笔退款，所以参数名会有编号
				throw new BusinessException("invalid ret out_refund_no " + retMap.get("out_refund_no_0") + "|" + refund.getRefundId());
			}
			if (StringUtils.isBlank(retMap.get("transaction_id"))) {
				throw new BusinessException("invalid refund response, transaction_id is empty. refundId=" + refund.getRefundId());
			}
			if (StringUtils.isBlank(retMap.get("refund_id_0"))) {
				throw new BusinessException("invalid refund response. refund_id is empty. refundId=" + refund.getRefundId());
			}
			String refundFeeStr = NumberUtils.formatBigDecimal(refund.getRefundAmount().multiply(new BigDecimal("100")), "#");
			if (!refundFeeStr.equals(retMap.get("refund_fee_0"))) {
				throw new BusinessException("invalid ret refund_fee. refundId=" + refund.getRefundId() + ", " + retMap.get("refund_fee_0") + "|" + refundFeeStr);
			}
			String totalFeeStr = NumberUtils.formatBigDecimal(refund.getPayAmount().multiply(new BigDecimal("100")), "#");
			if (!totalFeeStr.equals(retMap.get("total_fee"))) {
				throw new BusinessException("invalid ret total_fee. refundId=" + refund.getRefundId() + ", " + retMap.get("total_fee") + "|" + totalFeeStr);
			}

			result.setOrderId(Long.parseLong(retMap.get("out_trade_no")));
			result.setRefundId(refund.getRefundId());
			result.setRefundAmount(new BigDecimal(retMap.get("refund_fee")).divide(new BigDecimal(100)));
			String refundStatus = retMap.get("refund_status_0");
			if (WeixinPayConstant.REFUND_SUCCESS.equals(refundStatus)) {
				result.setRefundStatus(PayConstant.REFUND_STATUS_SUCC);
			} else if (WeixinPayConstant.REFUND_FAIL.equals(refundStatus)) {
				result.setRefundStatus(PayConstant.REFUND_STATUS_FAILED_NEED_RESEND);
			} else if (WeixinPayConstant.REFUND_PROCESSING.equals(refundStatus)) {
				result.setRefundStatus(PayConstant.REFUND_STATUS_SUCC);
			} else if (WeixinPayConstant.REFUND_NOTSURE.equals(refundStatus)) {
				result.setRefundStatus(PayConstant.REFUND_STATUS_FAILED_NEED_RESEND);
			} else if (WeixinPayConstant.REFUND_CHANGE.equals(refundStatus)) {
				result.setRefundStatus(PayConstant.REFUND_STATUS_FAILED);
			} else {
				throw new BusinessException("invalid refund status :" + refundStatus + ", refundId=" + refund.getRefundId());
			}
			return result;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException("query refund error. refundId=" + refund.getRefundId(), e);
		}
	}
	
	@Override
	public String payNotifyResponse(boolean success) {
		if (success) {
			return WeixinPayConstant.PAYNOTIFY_RESPONSE_SUCCESS;
		} else {
			return WeixinPayConstant.PAYNOTIFY_RESPONSE_FAIL;
		}
	}
}
