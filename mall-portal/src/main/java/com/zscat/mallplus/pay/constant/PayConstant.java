package com.zscat.mallplus.pay.constant;

public class PayConstant {

	/*******************************
	 * 充值状态 TB_REFUND.REFUND_STATUS
	 * 0 初始化
	 * 1 请求成功，未明确返回
	 * 2 失败需重发
	 * 3 成功
	 * 4 失败需人工处理
	 */
	public static final int REFUND_STATUS_INITIAL = 0;
	public static final int REFUND_STATUS_PROCESSED = 1;
	public static final int REFUND_STATUS_FAILED_NEED_RESEND = 2;
	public static final int REFUND_STATUS_SUCC = 3;
	public static final int REFUND_STATUS_FAILED = 4;
}
