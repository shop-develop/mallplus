package com.zscat.mallplus.pay.weixin;


import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.util.JacksonUtil;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.weixin.constant.WeixinPayConstant;
import com.zscat.mallplus.pay.weixin.util.WeixinPayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("weixin_jsapiPlatform")
public class WeixinJsapiPlatform extends AbstraceWeixinPlatform {

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		Map<String, String> preOrderRet = prepay(order, paymethod);
		
		if (preOrderRet == null) {
			return null;
		}
		String prepayId = preOrderRet.get("prepay_id");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("appId", paymethod.getAppId());
		map.put("nonceStr", WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
		map.put("timeStamp", String.valueOf((System.currentTimeMillis() / 1000)));
		map.put("package", "prepay_id=" + prepayId);
		map.put("signType", "MD5");
		
		String signStr = WeixinPayUtil.formatUnSignParaMap(map, false, false) + "&key=" + paymethod.getApiKey();
		System.out.println(signStr);
		String sign = DigestUtils.md5Hex(signStr).toUpperCase();
		map.put("paySign", sign);
		
		return JacksonUtil.obj2json(map);
	}



	@Override
	protected String getTradeType() {
		return WeixinPayConstant.TRADE_TYPE_JSAPI;
	}

}
