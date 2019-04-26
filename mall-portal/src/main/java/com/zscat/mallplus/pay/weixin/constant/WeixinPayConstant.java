package com.zscat.mallplus.pay.weixin.constant;

public class WeixinPayConstant {

	public static final int NONCESTR_LENGTH = 16; // 随机串长度

	// **********************以下常量为新接口用******************
	// 统一下单接口
	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 订单查询接口
	public static final String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 退款接口
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 退款查询接口
	public static final String QUERY_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	
	// 微信支付交易类型
	public static final String TRADE_TYPE_WAP = "MWEB"; // wap支付(H5支付)
	public static final String TRADE_TYPE_JSAPI = "JSAPI"; // 公众号支付
	public static final String TRADE_TYPE_APP = "APP"; // app支付
	public static final String TRADE_TYPE_NATIVE = "NATIVE"; // 二维码支付

	// 接口返回码-成功
	public static final String RETURN_CODE_SUCCESS = "SUCCESS";

	// 错误码
	public static final String ERROR_PAID = "ORDERPAID"; // 订单已支付
	public static final String SYSTEM_ERROR = "SYSTEMERROR"; // 微信系统内部异常，需要重试

	// 订单状态
	public static final String TRADE_SUCCESS = "SUCCESS";
	public static final String TRADE_REFUND = "REFUND";
	// 退款状态
	public static final String REFUND_SUCCESS = "SUCCESS"; // 退款成功
	public static final String REFUND_FAIL = "FAIL"; // 退款失败，需要用相同单号重新提交退款
	public static final String REFUND_PROCESSING = "PROCESSING"; // 处理中，代表请求已经提交给银行，但还没有到账。可以当成功处理
	public static final String REFUND_NOTSURE = "NOTSURE"; // 未确定，需要用相同退款单号重新请求
	public static final String REFUND_CHANGE = "CHANGE"; // 原路退回失败，可能是银行卡作废或者冻结了，需要手动转账退款

	// 支付成功接口的返回值
	public static final String PAYNOTIFY_RESPONSE_SUCCESS = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
	public static final String PAYNOTIFY_RESPONSE_FAIL = "<xml><return_code>FAIL</return_code><return_msg>FAIL</return_msg></xml>";
}
