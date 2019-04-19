package com.zscat.mallplus.marking.controller;

import com.zscat.mallplus.utils.CommonResult;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.zscat.mallplus.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.marking.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.annotation.SysLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 首页轮播广告表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "SmsHomeAdvertiseController", description = "首页轮播广告表管理")
@RequestMapping("/marking/SmsHomeAdvertise")
public class SmsHomeAdvertiseController {
    @Resource
    private ISmsHomeAdvertiseService ISmsHomeAdvertiseService;

    @SysLog(MODULE = "marking", REMARK = "根据条件查询所有首页轮播广告表列表")
    @ApiOperation("根据条件查询所有首页轮播广告表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:read')")
    public Object getSmsHomeAdvertiseByPage(SmsHomeAdvertise entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsHomeAdvertiseService.page(new Page<SmsHomeAdvertise>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有首页轮播广告表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "保存首页轮播广告表")
    @ApiOperation("保存首页轮播广告表")
    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:create')")
    public Object saveSmsHomeAdvertise(@RequestBody SmsHomeAdvertise entity) {
        try {
            if (ISmsHomeAdvertiseService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存首页轮播广告表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "更新首页轮播广告表")
    @ApiOperation("更新首页轮播广告表")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:update')")
    public Object updateSmsHomeAdvertise(@RequestBody SmsHomeAdvertise entity) {
        try {
            if (ISmsHomeAdvertiseService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新首页轮播广告表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "删除首页轮播广告表")
    @ApiOperation("删除首页轮播广告表")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:delete')")
    public Object deleteSmsHomeAdvertise(@ApiParam("首页轮播广告表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("首页轮播广告表id");
            }
            if (ISmsHomeAdvertiseService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除首页轮播广告表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "给首页轮播广告表分配首页轮播广告表")
    @ApiOperation("查询首页轮播广告表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:read')")
    public Object getSmsHomeAdvertiseById(@ApiParam("首页轮播广告表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("首页轮播广告表id");
            }
            SmsHomeAdvertise coupon = ISmsHomeAdvertiseService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询首页轮播广告表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除首页轮播广告表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除首页轮播广告表")
    @PreAuthorize("hasAuthority('marking:SmsHomeAdvertise:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ISmsHomeAdvertiseService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
