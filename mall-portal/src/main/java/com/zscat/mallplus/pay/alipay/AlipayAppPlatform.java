package com.zscat.mallplus.pay.alipay;


import com.zscat.mallplus.pay.alipay.constant.AlipayConstant;
import com.zscat.mallplus.pay.alipay.util.AlipaySignUtil;
import com.zscat.mallplus.pay.util.NumberUtils;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.PayMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service("alipay_appPlatform")
public class AlipayAppPlatform extends AbstraceAlipayPlatform {

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		// 1.创建用于签名的参数map
		Map<String, String> paramMap = genParamMap(order, paymethod);
		// 2.生成并添加签名
		String sign = AlipaySignUtil.sign(paramMap, AlipayConstant.INPUT_CHARSET, AlipayConstant.REQUEST_SIGN_TYPE, paymethod);
		try {
			addQuoteStringToMap(paramMap, "sign", URLEncoder.encode(sign, "utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
		// 3.生成组合字符串
		return AlipaySignUtil.getParamStr(paramMap, false, AlipayConstant.INPUT_CHARSET);
	}
	
	private Map<String, String> genParamMap(PayOrder order, PayMethod paymethod) {
		Map<String, String> signParaMap = new HashMap<String, String>();
		// 基本参数
		// 连接所有非空参数
		addQuoteStringToMap(signParaMap, "service", AlipayConstant.APP_SECURITYPAY_PAY_SERVICE_NAME);
		addQuoteStringToMap(signParaMap, "partner", paymethod.getPartnerId());
		addQuoteStringToMap(signParaMap, "_input_charset", AlipayConstant.INPUT_CHARSET);
		addQuoteStringToMap(signParaMap, "notify_url", paymethod.getPayNotifyUrl(order));
		addQuoteStringToMap(signParaMap, "sign_type", AlipaySignUtil.SIGN_TYPE_RSA);
		
		// 业务参数
		// 连接所有非空参数
		addQuoteStringToMap(signParaMap, "out_trade_no", order.getPayOrderId());
		addQuoteStringToMap(signParaMap, "subject", order.getGoodsName());
		addQuoteStringToMap(signParaMap, "payment_type", "1");
		addQuoteStringToMap(signParaMap, "seller_id", paymethod.getPartnerId());
		addQuoteStringToMap(signParaMap, "total_fee", NumberUtils.formatBigDecimal(order.getPayAmount(), "0.00"));
		addQuoteStringToMap(signParaMap, "body", order.getGoodsName());

		return signParaMap;
	}
	
	private void addQuoteStringToMap(Map<String, String> map, String key, String value) {
		if(map!=null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
			map.put(key, quote(value));
		}
	}
	
	private String quote(String str) {
		return "\"" + str + "\"";
	}
}
