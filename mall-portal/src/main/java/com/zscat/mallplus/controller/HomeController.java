package com.zscat.mallplus.controller;


import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.constant.RedisKey;
import com.zscat.mallplus.marking.entity.SmsCoupon;
import com.zscat.mallplus.marking.service.ISmsCouponService;
import com.zscat.mallplus.marking.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页内容管理Controller
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/api/home")
public class HomeController {
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



    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public Object content() {
        HomeContentResult contentResult = null;
        String bannerJson = redisService.get(RedisKey.HomeContentResult);
        if(bannerJson!=null){
            contentResult = JsonUtil.jsonToPojo(bannerJson,HomeContentResult.class);
        }else {
            contentResult = advertiseService.singelContent();
            redisService.set(RedisKey.HomeContentResult,JsonUtil.objectToJson(contentResult));
            redisService.expire(RedisKey.HomeContentResult,24*60*60);
        }
        return new CommonResult().success(contentResult);
    }
    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/pc/content", method = RequestMethod.GET)
    public Object pcContent() {
        HomeContentResult contentResult = null;
        String bannerJson = redisService.get(RedisKey.HomeContentResult);
        if(bannerJson!=null){
            contentResult = JsonUtil.jsonToPojo(bannerJson,HomeContentResult.class);
        }else {
            contentResult = advertiseService.singelContent();
            redisService.set(RedisKey.HomeContentResult,JsonUtil.objectToJson(contentResult));
            redisService.expire(RedisKey.HomeContentResult,24*60*60);
        }
        return new CommonResult().success(contentResult);
    }


    @IgnoreAuth
    @ApiOperation("分页获取最热商品")
    @SysLog(MODULE = "home", REMARK = "分页获取最热商品")
    @RequestMapping(value = "/hotProductList", method = RequestMethod.GET)
    public Object hotProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = advertiseService.getHotProductList(pageSize, pageNum);
        return new CommonResult().success(productList);
    }
    @IgnoreAuth
    @ApiOperation("分页获取最新商品")
    @SysLog(MODULE = "home", REMARK = "分页获取最新商品")
    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)
    public Object newProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = advertiseService.getNewProductList(pageSize, pageNum);
        return new CommonResult().success(productList);
    }

    @IgnoreAuth
    @ApiOperation("根据分类获取专题")
    @SysLog(MODULE = "home", REMARK = "根据分类获取专题")
    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
    public Object getSubjectList(
                                 @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = advertiseService.getRecommendSubjectList(pageSize, pageNum);
        return new CommonResult().success(subjectList);
    }
    @IgnoreAuth
    @GetMapping(value = "/subjectDetail")
    @SysLog(MODULE = "home", REMARK = "据分类获取专题")
    @ApiOperation(value = "据分类获取专题")
    public Object subjectDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        CmsSubject cmsSubject = subjectService.getById(id);
        UmsMember umsMember = memberService.getCurrentMember();
        /*if (umsMember != null && umsMember.getId() != null) {
            MemberProductCollection findCollection = productCollectionRepository.findByMemberIdAndProductId(
                    umsMember.getId(), id);
            if(findCollection!=null){
                cmsSubject.setIs_favorite(1);
            }else{
                cmsSubject.setIs_favorite(2);
            }
        }*/
        return new CommonResult().success(cmsSubject);
    }





    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "获取导航栏")
    @RequestMapping(value = "/navList",method = RequestMethod.GET)
    @ApiOperation(value = "获取导航栏")
    public Object getNavList(){

        return new CommonResult().success(null);
    }



    @IgnoreAuth
    @ApiOperation("分页获取推荐商品")
    @RequestMapping(value = "/getHomeCouponList", method = RequestMethod.GET)
    public Object getHomeCouponList() {
        List<SmsCoupon> couponList = couponService.selectNotRecive();
        return new CommonResult().success(couponList);
    }
}
