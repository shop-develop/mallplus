package com.zscat.mallplus.pay.alipay.constant;

import com.zscat.mallplus.pay.alipay.util.AlipaySignUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlipayConstant {
	// 支付宝网关地址，pc端、app支付、订单查询用这个
	public static String ALIPAY_GATEWAY = "https://mapi.alipay.com/gateway.do?";

	// 请求编码，支付宝所有用到编码的地方统一用这个
	public static String INPUT_CHARSET = "utf-8";

	// 请求的签名方式
	public static String REQUEST_SIGN_TYPE = AlipaySignUtil.SIGN_TYPE_RSA;

	// 即时到帐交易（pc）service_name
	public static final String DIRECT_PAY_SERVICE_NAME = "create_direct_pay_by_user";
	// 客户支付service_name
	public static final String APP_SECURITYPAY_PAY_SERVICE_NAME = "mobile.securitypay.pay";
	// WAP即时到帐交易service_name
	public static final String WAP_CREATE_DIRECT_SERVICE_NAME = "alipay.wap.create.direct.pay.by.user";
	// 单笔订单查询service_name
	public static String CHARGE_QUERY_SERVICE_NAME = "single_trade_query";
	// 无密退款接口service_name
	public static String REFUND_SERVICE_NAME = "refund_fastpay_by_platform_nopwd";
	
	// 支付宝交易状态
	public static String TRADE_SUCCESS = "TRADE_SUCCESS";
	public static String TRADE_FINISHD = "TRADE_FINISHED";
	
	// 退款成功
	public static String REFUND_SUCCESS = "REFUND_SUCCESS";
	
	/** 支付成功通知接口中必须字段 */
	@SuppressWarnings("serial")
	public static List<String> PAYSUCCESS_NOTICE_NOTNULL_FIELD_LIST = Collections
			.unmodifiableList(new ArrayList<String>() {
				{
					add("notify_type");
					add("notify_id");
					add("sign_type");
					add("sign");
					add("out_trade_no");
					add("trade_no");
					add("trade_status");
					// add("gmt_payment");
				}
			});
	
	/** 支付成功通知接口中必须字段 */
	@SuppressWarnings("serial")
	public static List<String> REFUND_NOTICE_NOTNULL_FIELD_LIST = Collections
			.unmodifiableList(new ArrayList<String>() {
				{
					add("notify_type");
					add("notify_id");
					add("sign_type");
					add("sign");
					add("batch_no");
					add("success_num");
					add("result_details");
				}
			});

	// 订单查询
	public static String API_RET_SUCCESS = "T";

}
