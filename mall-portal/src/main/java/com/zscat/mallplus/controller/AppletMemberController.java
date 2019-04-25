package com.zscat.mallplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.constant.RedisKey;
import com.zscat.mallplus.marking.entity.SmsCoupon;
import com.zscat.mallplus.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.marking.entity.SmsRedPacket;
import com.zscat.mallplus.marking.entity.SmsUserRedPacket;
import com.zscat.mallplus.marking.service.ISmsCouponService;
import com.zscat.mallplus.marking.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.marking.service.ISmsRedPacketService;
import com.zscat.mallplus.marking.service.ISmsUserRedPacketService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.service.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.IndexData;
import com.zscat.mallplus.vo.OrderStatusCount;
import com.zscat.mallplus.vo.TArticleDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员登录注册管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/3.
 */
@RestController
@Api(tags = "AppletMemberController", description = "小程序登录首页")
@RequestMapping("/api/applet")
public class AppletMemberController extends ApiBaseAction {
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



    @Resource
    private ISmsRedPacketService redPacketService;

    @Resource
    private ISmsUserRedPacketService userRedPacketService;


    @IgnoreAuth
    @ApiOperation("注册")
    @SysLog(MODULE = "applet", REMARK = "小程序注册")
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(HttpServletRequest req) {
        return memberService.loginByWeixin(req);

    }

    /**
     * 小程序主页
     *
     * @param
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "applet", REMARK = "小程序首页")
    @ApiOperation("小程序首页")
    @GetMapping("/index")
    public Object index() {

        List<TArticleDO> model_list = new ArrayList<>();
        List<TArticleDO> nav_icon_list = new ArrayList<>();
        IndexData data = new IndexData();
        try {
            TArticleDO a = new TArticleDO("banner");
            TArticleDO a1 = new TArticleDO("search");
            TArticleDO a2 = new TArticleDO("nav");
            TArticleDO a3 = new TArticleDO("cat");
            TArticleDO a4 = new TArticleDO("coupon");
            TArticleDO a5 = new TArticleDO("topic");
            TArticleDO a6 = new TArticleDO("redPacket");
            TArticleDO b2 = new TArticleDO("block", "3");
            TArticleDO b1 = new TArticleDO("block", "4");
            TArticleDO b3 = new TArticleDO("block", "5");
            model_list.add(a);
            model_list.add(a1);
            model_list.add(a2);
            model_list.add(a3);
            model_list.add(a4);
            model_list.add(a5);
            model_list.add(a6);
            model_list.add(b1);
            model_list.add(b2);
            model_list.add(b3);
            List<SmsHomeAdvertise> bannerList = null;
            String bannerJson = redisService.get(RedisKey.appletBannerKey + "2");
            if (bannerJson != null) {
                bannerList = JsonUtil.jsonToList(bannerJson, SmsHomeAdvertise.class);
            } else {
                SmsHomeAdvertise queryT = new SmsHomeAdvertise();
                queryT.setType(2);
                bannerList = advertiseService.list(new QueryWrapper<>(queryT));
                redisService.set(RedisKey.appletBannerKey + "2", JsonUtil.objectToJson(bannerList));
                redisService.expire(RedisKey.appletBannerKey + "2", 24 * 60 * 60);
            }
            List<SmsCoupon> couponList = new ArrayList<>();

            couponList = couponService.selectNotRecive();


            TArticleDO c1 = new TArticleDO("我的公告", "/pages/topic-list/topic-list", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/86/863a7db352a936743faf8edd5162bb5c.png");
            TArticleDO c2 = new TArticleDO("商品分类", "/pages/cat/cat", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/35/3570994c06e61b1f0cf719bdb52a0053.png");
            TArticleDO c3 = new TArticleDO("购物车", "/pages/cart/cart", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/c2/c2b01cf78f79cbfba192d5896eeaecbe.png");
            TArticleDO c4 = new TArticleDO("我的订单", "/pages/order/order?status=9", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/7c/7c80acbbd479b099566cc6c3d34fbcb8.png");
            TArticleDO c5 = new TArticleDO("用户中心", "/pages/user/user", "switchTab", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/46/46eabbff1e7dc5e416567fc45d4d5df3.png");
            TArticleDO c6 = new TArticleDO("优惠劵", "/pages/coupon/coupon?status=0", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/13/13312a6d56c202330f8c282d8cf84ada.png");
            TArticleDO c7 = new TArticleDO("我的收藏", "/pages/favorite/favorite", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/ca/cab6d8d4785e43bd46dcbb52ddf66f61.png");
            TArticleDO c8 = new TArticleDO("售后订单", "/pages/order/order?status=4", "navigate", "http://www.91weiyi.xyz/addons/zjhj_mall/core/web/uploads/image/cf/cfb32a65d845b4e9a9778020ed2ccac6.png");
            nav_icon_list.add(c1);
            nav_icon_list.add(c2);
            nav_icon_list.add(c3);
            nav_icon_list.add(c4);
            nav_icon_list.add(c5);
            nav_icon_list.add(c6);
            nav_icon_list.add(c7);
            nav_icon_list.add(c8);


            List<PmsProductAttributeCategory> productAttributeCategoryList = null;
            String catJson = redisService.get(RedisKey.appletCategoryKey);
            if (catJson == null) {
                productAttributeCategoryList = JsonUtil.jsonToList(catJson, PmsProductAttributeCategory.class);
            } else {
                productAttributeCategoryList = productAttributeCategoryService.list(new QueryWrapper<>());
                for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
                    PmsProduct productQueryParam = new PmsProduct();
                    productQueryParam.setProductAttributeCategoryId(gt.getId());
                    productQueryParam.setPublishStatus(1);
                    productQueryParam.setVerifyStatus(1);
                    gt.setGoodsList(pmsProductService.list(new QueryWrapper<>(productQueryParam)));
                }
                redisService.set(RedisKey.appletCategoryKey, JsonUtil.objectToJson(productAttributeCategoryList));
                redisService.expire(RedisKey.appletCategoryKey, 24 * 60 * 60);
            }
            List<CmsSubject> subjectList = subjectService.list(new QueryWrapper<>());
            data.setSubjectList(subjectList);
            data.setCat_goods_cols(2);
            data.setCat_list(productAttributeCategoryList);
            data.setNav_icon_list(nav_icon_list);
            data.setBanner_list(bannerList);
            data.setCoupon_list(couponList);
            data.setModule_list(model_list);
            List<SmsRedPacket> redPacketList = redPacketService.list(new QueryWrapper<>());
            SmsUserRedPacket userRedPacket = new SmsUserRedPacket();
            userRedPacket.setUserId(UserUtils.getCurrentMember().getId());
            List<SmsUserRedPacket> list = userRedPacketService.list(new QueryWrapper<>(userRedPacket));
            for(SmsRedPacket vo : redPacketList){
                if (list!=null && list.size()>0){
                    for (SmsUserRedPacket vo1 : list){
                        if(vo.getId().equals(vo1.getRedPacketId())){
                            vo.setStatus(1);
                            vo.setReciveAmount(vo1.getAmount());
                            break;
                        }
                    }
                }
            }
            data.setRedPacketList(redPacketList);
            return new CommonResult().success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    @IgnoreAuth
    @ApiOperation("小程序用户详情")
    @SysLog(MODULE = "applet", REMARK = "小程序用户详情")
    @GetMapping("/user")
    public Object user() {
        UmsMember umsMember = UserUtils.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            OmsOrder param = new OmsOrder();
            param.setMemberId(umsMember.getId());
            List<OmsOrder> list = orderService.list(new QueryWrapper<>(param));
            int status0 = 0;
            int status1 = 0;
            int status2 = 0;
            int status3 = 0;
            int status4 = 0;
            int status5 = 0;
            OrderStatusCount count = new OrderStatusCount();
            for (OmsOrder consult : list) {
                if (consult.getStatus() == 0) {
                    status0++;
                }
                if (consult.getStatus() == 1) {
                    status1++;
                }
                if (consult.getStatus() == 2) {
                    status2++;
                }
                if (consult.getStatus() == 3) {
                    status2++;
                }
                if (consult.getStatus() == 4) {
                    status4++;
                }
                if (consult.getStatus() == 5) {
                    status5++;
                }
            }
            count.setStatus0(status0);
            count.setStatus1(status1);
            count.setStatus2(status2);
            count.setStatus3(status3);
            count.setStatus4(status4);
            count.setStatus5(status5);
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("user", umsMember);
            objectMap.put("count", count);
            return new CommonResult().success(objectMap);
        }
        return new CommonResult().failed();

    }
}
