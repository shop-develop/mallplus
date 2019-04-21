package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.constant.RedisKey;
import com.zscat.mallplus.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.marking.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
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
 * https://gitee.com/zscat-platform/mall on 2019/1/28.
 */
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/api/single/home")
public class SingelHomeController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private IOmsOrderService orderService;
    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public Object content() {
        HomeContentResult contentResult = advertiseService.singelContent();
        return new CommonResult().success(contentResult);
    }


    /**
     * banner
     *
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "bannerList")
    @GetMapping("/bannerList")
    public Object bannerList(@RequestParam(value = "type", required = false, defaultValue = "10") Integer type) {
        List<SmsHomeAdvertise> bannerList = null;
        String bannerJson = redisService.get(RedisKey.appletBannerKey+type);
        if(bannerJson!=null && bannerJson!="[]"){
            bannerList = JsonUtil.jsonToList(bannerJson,SmsHomeAdvertise.class);
        }else {
            SmsHomeAdvertise advertise = new SmsHomeAdvertise();
            advertise.setType(type);
            bannerList = advertiseService.list(new QueryWrapper<>(advertise));
            redisService.set(RedisKey.appletBannerKey+type,JsonUtil.objectToJson(bannerList));
            redisService.expire(RedisKey.appletBannerKey+type,24*60*60);
        }
      //  List<SmsHomeAdvertise> bannerList = advertiseService.list(null, type, null, 5, 1);
        return new CommonResult().success(bannerList);
    }

}
