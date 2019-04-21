package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.vo.ConfirmOrderResult;
import com.zscat.mallplus.oms.vo.GroupAndOrderVo;
import com.zscat.mallplus.oms.vo.OrderParam;
import com.zscat.mallplus.oms.vo.TbThanks;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.utils.CommonResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderService extends IService<OmsOrder> {

    Object preSingelOrder(GroupAndOrderVo orderParam);

    Object generateSingleOrder(GroupAndOrderVo orderParam, UmsMember member);

    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder();

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam);

    /**
     * 支付成功后的回调
     */
    @Transactional
    CommonResult paySuccess(Long orderId);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    void cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(Long orderId);


    ConfirmOrderResult submitPreview(OrderParam orderParam);

    int payOrder(TbThanks tbThanks);
}
