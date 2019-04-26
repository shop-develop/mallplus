package com.zscat.mallplus.pay;


import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.pay.alipay.AlipayAppPlatform;
import com.zscat.mallplus.pay.alipay.AlipayPcPlatform;
import com.zscat.mallplus.pay.alipay.AlipayWapPlatform;
import com.zscat.mallplus.pay.util.SpringContextHolder;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.vo.PayOrder.PayOrderType;
import com.zscat.mallplus.pay.weixin.WeixinAppPlatform;
import com.zscat.mallplus.pay.weixin.WeixinJsapiPlatform;
import com.zscat.mallplus.pay.weixin.WeixinWapPlatform;
import com.zscat.mallplus.pay.weixin.WinxinNativePlatform;

public enum PayMethods implements PayMethod {

	alipay_wap(AlipayWapPlatform.class),
	alipay_app(AlipayAppPlatform.class),
	alipay_pc(AlipayPcPlatform.class),
	weixin_wap(WeixinWapPlatform.class),
	weixin_app(WeixinAppPlatform.class),
	weixin_jsapi(WeixinJsapiPlatform.class),
	weixin_native(WinxinNativePlatform.class),
	exchange(null), //??? No such bean
	free(BypassForFreePlatform.class); // 虚拟支付。用于减脂营兑换生成的订单

	private Class<? extends PayPlatform> payPlatformClass;
	PayMethods(Class<? extends PayPlatform> clazz) {
		this.payPlatformClass = clazz;
	}
	
	@Override
	public PayPlatform getPayPlatform() {
//		String beanName = this.name() + "Platform";
		return SpringContextHolder.getApplicationContext().getBean(payPlatformClass);
//		return SpringContextHolder.getBean(beanName);
	}

	/**
	 * 获取商户ID
	 * @return
	 */
	@Override
	public String getPartnerId() {
		return ConfigUtil.getString("pay." + this.name() + ".partnerId");
	}

	/**
	 * 获取appid，微信支付用
	 * @return
	 */
	@Override
	public String getAppId() {
		return ConfigUtil.getString("pay." + this.name() + ".appId");
	}

	/**
	 * 获取支付key。对于支付宝来说是md5key
	 * @return
	 */
	@Override
	public String getApiKey() {
		return ConfigUtil.getString("pay." + this.name() + ".apiKey");
	}

	/**
	 * 获取rsa公钥。支付宝用
	 * @return
	 */
	@Override
	public String getPublicKey() {
		return ConfigUtil.getString("pay." + this.name() + ".pubKey");
	}

	/**
	 * 支付成功通知url
	 * @return
	 */
	@Override
	public String getPayNotifyUrl(PayOrder payOrder) {
		if (payOrder.getPayOrderType() == PayOrderType.diet) {
			return CommonConstant.DOMAIN + "/pay/" + payOrder.getPayOrderType().name() + "/" + this.name() + "/payNotify.html";
		} else {
			if ("useNewSuccessPage".equals(payOrder.getParams())) {
				return CommonConstant.DOMAIN + "/pay/" + this.name() + "/payNotifyNew.html";
			} else {
				return CommonConstant.DOMAIN + "/pay/" + this.name() + "/payNotify.html";
			}
		}
		
	}

	/**
	 * 退款通知接口
	 * @return
	 */
	@Override
	public String getRefundNotifyUrl(PayOrderType payOrderType) {
		return CommonConstant.DOMAIN + "/pay/" + payOrderType.name() + "/" + this.name() + "/refundNotify.html";
	}

	/**
	 * 生成支付Url
	 * @param payOrder
	 * @return
	 */
	@Override
	public String generatePayUrl(PayOrder payOrder) {
		return getPayPlatform().generatePayUrl(payOrder, this);
	}
}
