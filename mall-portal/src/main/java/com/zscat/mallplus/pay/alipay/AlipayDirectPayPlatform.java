package com.zscat.mallplus.pay.alipay;

import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.alipay.constant.AlipayConstant;
import com.zscat.mallplus.pay.alipay.util.AlipaySignUtil;
import com.zscat.mallplus.pay.util.NumberUtils;
import com.zscat.mallplus.pay.vo.PayOrder;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class AlipayDirectPayPlatform extends AbstraceAlipayPlatform {
	
	protected abstract String getServiceName();

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", getServiceName());
		map.put("partner", paymethod.getPartnerId());
		map.put("_input_charset", AlipayConstant.INPUT_CHARSET);
		map.put("sign_type", AlipayConstant.REQUEST_SIGN_TYPE);
		map.put("notify_url", paymethod.getPayNotifyUrl(order));
		map.put("return_url", paymethod.getPayNotifyUrl(order));
		map.put("out_trade_no", order.getPayOrderId());
		map.put("subject", order.getGoodsName());
		map.put("payment_type", "1");
		map.put("total_fee", NumberUtils.formatBigDecimal(order.getPayAmount(), "0.00"));
		map.put("seller_id", paymethod.getPartnerId());
		if (StringUtils.isNotBlank(order.getParams())) {
			map.put("passback_params", order.getParams());
		}
		map.put("sign", AlipaySignUtil.sign(map, AlipayConstant.INPUT_CHARSET, AlipayConstant.REQUEST_SIGN_TYPE, paymethod));
		String paramStr = AlipaySignUtil.getParamStr(map, true, AlipayConstant.INPUT_CHARSET);
		
		return AlipayConstant.ALIPAY_GATEWAY + paramStr;
	}
}
