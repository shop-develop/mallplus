package com.zscat.mallplus.pay.weixin;


import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.util.FilePathUtil;
import com.zscat.mallplus.pay.util.QRCodeUtil;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.weixin.constant.WeixinPayConstant;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service("weixin_nativePlatform")
public class WinxinNativePlatform extends AbstraceWeixinPlatform{

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		Map<String, String> preOrderRet = prepay(order, paymethod);
		
		if (preOrderRet == null) {
			return null;
		}
		String codeUrl = preOrderRet.get("code_url");
		
		String wxlogo = FilePathUtil.getAbsolutePathOfClassPath() + "/wxlogo.jpg";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QRCodeUtil.buildQrCodeWithLogo(codeUrl, wxlogo, out);
		String qrcodeStr = Base64.encodeBase64String(out.toByteArray());
		
		return "data:image/jpeg;base64," + qrcodeStr;
	}

	@Override
	protected String getTradeType() {
		return WeixinPayConstant.TRADE_TYPE_NATIVE;
	}

}
