package com.zscat.mallplus.pms.controller;

import com.zscat.mallplus.utils.CommonResult;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.zscat.mallplus.pms.entity.PmsProductCategory;
import com.zscat.mallplus.pms.service.IPmsProductCategoryService;
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
 * 产品分类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductCategoryController", description = "产品分类管理")
@RequestMapping("/pms/PmsProductCategory")
public class PmsProductCategoryController {
    @Resource
    private IPmsProductCategoryService IPmsProductCategoryService;

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有产品分类列表")
    @ApiOperation("根据条件查询所有产品分类列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:read')")
    public Object getPmsProductCategoryByPage(PmsProductCategory entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IPmsProductCategoryService.page(new Page<PmsProductCategory>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有产品分类列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "保存产品分类")
    @ApiOperation("保存产品分类")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:create')")
    public Object savePmsProductCategory(@RequestBody PmsProductCategory entity) {
        try {
            if (IPmsProductCategoryService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存产品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "更新产品分类")
    @ApiOperation("更新产品分类")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:update')")
    public Object updatePmsProductCategory(@RequestBody PmsProductCategory entity) {
        try {
            if (IPmsProductCategoryService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新产品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "删除产品分类")
    @ApiOperation("删除产品分类")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:delete')")
    public Object deletePmsProductCategory(@ApiParam("产品分类id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("产品分类id");
            }
            if (IPmsProductCategoryService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除产品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "给产品分类分配产品分类")
    @ApiOperation("查询产品分类明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:read')")
    public Object getPmsProductCategoryById(@ApiParam("产品分类id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("产品分类id");
            }
            PmsProductCategory coupon = IPmsProductCategoryService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询产品分类明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除产品分类")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除产品分类")
    @PreAuthorize("hasAuthority('pms:PmsProductCategory:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IPmsProductCategoryService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
