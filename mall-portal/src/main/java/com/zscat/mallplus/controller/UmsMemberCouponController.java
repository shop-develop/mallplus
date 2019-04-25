package com.zscat.mallplus.controller;


import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.marking.entity.SmsCoupon;
import com.zscat.mallplus.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.marking.service.ISmsCouponService;
import com.zscat.mallplus.marking.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.oms.service.IOmsCartItemService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.CartPromotionItem;
import com.zscat.mallplus.pms.service.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户优惠券管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/api/member/coupon")
public class UmsMemberCouponController {
    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private ISmsCouponService couponService;
    @Autowired
    private IPmsProductAttributeCategoryService productAttributeCategoryService;

    @Autowired
    private IPmsProductService pmsProductService;

    @Autowired
    private ICmsSubjectService subjectService;
    @Autowired
    private IOmsOrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IOmsCartItemService cartItemService;

    @ApiOperation("领取指定优惠券")
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add( Long couponId) {
        return couponService.add(couponId);
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = couponService.list(useStatus);
        return new CommonResult().success(couponHistoryList);
    }

    /**
     * 所有可领取的优惠券
     * @return
     */
    @RequestMapping(value = "/alllist", method = RequestMethod.GET)
    @ResponseBody
    public Object alllist() {
        List<SmsCoupon> couponList = new ArrayList<>();
        couponList = couponService.selectNotRecive();
        return new CommonResult().success(couponList);
    }


    @ApiOperation("获取登录会员购物车的相关优惠券")
    @ApiImplicitParam(name = "type", value = "使用可用:0->不可用；1->可用",
            defaultValue = "1", allowableValues = "0,1", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list/cart/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(),null);
        List<SmsCouponHistoryDetail> couponHistoryList = couponService.listCart(cartPromotionItemList, type);
        return new CommonResult().success(couponHistoryList);
    }

}
