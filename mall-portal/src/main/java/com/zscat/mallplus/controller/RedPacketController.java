package com.zscat.mallplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.marking.entity.SmsRedPacket;
import com.zscat.mallplus.marking.entity.SmsUserRedPacket;
import com.zscat.mallplus.marking.service.ISmsRedPacketService;
import com.zscat.mallplus.marking.service.ISmsUserRedPacketService;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 红包
 *
 * @author zscat
 * @email 951449465@qq.com
 * @date 2019-04-05 16:20:35
 */
@Controller
@Api(tags = "RedPacketController", description = "红包管理")
@RequestMapping("/api/redPacket")
public class RedPacketController {
    @Resource
    private ISmsRedPacketService redPacketService;
    @Resource
    private ISmsUserRedPacketService userRedPacketService;
    @SysLog(MODULE = "sms", REMARK = "添加红包")
    @ApiOperation(value = "添加红包")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@Validated @RequestBody SmsRedPacket smsRedPacket, BindingResult result) {
        CommonResult commonResult;
        int count = redPacketService.createRedPacket(smsRedPacket);
        if (count == 1) {
            commonResult = new CommonResult().success(count);
        } else {
            commonResult = new CommonResult().failed();
        }
        return commonResult;
    }
    @SysLog(MODULE = "sms", REMARK = "更新红包")
    @ApiOperation(value = "更新红包")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@Validated @RequestBody SmsRedPacket smsRedPacket,
                         BindingResult result) {
        CommonResult commonResult;
        boolean count = redPacketService.updateById(smsRedPacket);
        if (count) {
            commonResult = new CommonResult().success(count);
        } else {
            commonResult = new CommonResult().failed();
        }
        return commonResult;
    }
    @SysLog(MODULE = "sms", REMARK = "删除红包")
    @ApiOperation(value = "删除红包")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable("id") Integer id) {
        boolean count = redPacketService.removeById(id);
        if (count) {
            return new CommonResult().success(null);
        } else {
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "sms", REMARK = "根据红包分页获取红包列表")
    @ApiOperation(value = "根据红包名称分页获取红包列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(SmsRedPacket redPacket,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<SmsRedPacket> redPacketList = redPacketService.list(new QueryWrapper<>(redPacket));

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

        return new CommonResult().success(redPacketList);
    }
    @SysLog(MODULE = "sms", REMARK = "根据编号查询红包信息")
    @ApiOperation(value = "根据编号查询红包信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object getItem(@PathVariable("id") Integer id) {
        return new CommonResult().success(redPacketService.getById(id));
    }

    @SysLog(MODULE = "sms", REMARK = "领取红包")
    @ApiOperation(value = "领取红包")
    @RequestMapping(value = "/accept", method = RequestMethod.GET)
    @ResponseBody
    public Object accept(Integer id) {
        int count = redPacketService.acceptRedPacket(id);
        if (count == 1) {
            return new CommonResult().success("领取成功");
        } else {
            return new CommonResult().failed("你已经领取此红包");
        }
    }
}
