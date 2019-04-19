package com.zscat.mallplus.pms.controller;

import com.zscat.mallplus.utils.CommonResult;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductService;
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
 * 商品信息
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductController", description = "商品信息管理")
@RequestMapping("/pms/PmsProduct")
public class PmsProductController {
    @Resource
    private IPmsProductService IPmsProductService;

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有商品信息列表")
    @ApiOperation("根据条件查询所有商品信息列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductByPage(PmsProduct entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IPmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有商品信息列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "保存商品信息")
    @ApiOperation("保存商品信息")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:PmsProduct:create')")
    public Object savePmsProduct(@RequestBody PmsProduct entity) {
        try {
            if (IPmsProductService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "更新商品信息")
    @ApiOperation("更新商品信息")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:update')")
    public Object updatePmsProduct(@RequestBody PmsProduct entity) {
        try {
            if (IPmsProductService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "删除商品信息")
    @ApiOperation("删除商品信息")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deletePmsProduct(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            if (IPmsProductService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除商品信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "给商品信息分配商品信息")
    @ApiOperation("查询商品信息明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProduct:read')")
    public Object getPmsProductById(@ApiParam("商品信息id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品信息id");
            }
            PmsProduct coupon = IPmsProductService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品信息明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除商品信息")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除商品信息")
    @PreAuthorize("hasAuthority('pms:PmsProduct:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IPmsProductService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
