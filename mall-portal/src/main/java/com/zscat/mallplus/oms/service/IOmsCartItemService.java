package com.zscat.mallplus.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.entity.OmsCartItem;
import com.zscat.mallplus.oms.vo.CartProduct;
import com.zscat.mallplus.oms.vo.CartPromotionItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsCartItemService extends IService<OmsCartItem> {

    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     */
    @Transactional
    OmsCartItem add(OmsCartItem cartItem);

    /**
     * 根据会员编号获取购物车列表
     */
    List<OmsCartItem> list(Long memberId, List<Long> ids);

    OmsCartItem selectById(Long id);

    /**
     * 获取包含促销活动信息的购物车列表
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> ids);

    /**
     * 修改某个购物车商品的数量
     */
    int updateQuantity(Long id, Long memberId, Integer quantity);

    /**
     * 批量删除购物车中的商品
     */
    int delete(Long memberId, List<Long> ids);

    /**
     * 获取购物车中用于选择商品规格的商品信息
     */
    CartProduct getCartProduct(Long productId);

    /**
     * 修改购物车中商品的规格
     */
    @Transactional
    int updateAttr(OmsCartItem cartItem);

    /**
     * 清空购物车
     */
    int clear(Long memberId);

    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);

    OmsCartItem addCart(OmsCartItem cartItem);
}
