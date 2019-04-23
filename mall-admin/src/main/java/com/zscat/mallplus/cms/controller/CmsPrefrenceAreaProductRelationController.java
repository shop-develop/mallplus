package com.zscat.mallplus.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.pms.entity.CmsPrefrenceAreaProductRelation;
import com.zscat.mallplus.cms.service.ICmsPrefrenceAreaProductRelationService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 优选专区和产品关系表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "CmsPrefrenceAreaProductRelationController", description = "优选专区和产品关系表管理")
@RequestMapping("/cms/CmsPrefrenceAreaProductRelation")
public class CmsPrefrenceAreaProductRelationController {
    @Resource
    private ICmsPrefrenceAreaProductRelationService ICmsPrefrenceAreaProductRelationService;

    @SysLog(MODULE = "cms", REMARK = "根据条件查询所有优选专区和产品关系表列表")
    @ApiOperation("根据条件查询所有优选专区和产品关系表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:read')")
    public Object getCmsPrefrenceAreaProductRelationByPage(CmsPrefrenceAreaProductRelation entity,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ICmsPrefrenceAreaProductRelationService.page(new Page<CmsPrefrenceAreaProductRelation>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有优选专区和产品关系表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "保存优选专区和产品关系表")
    @ApiOperation("保存优选专区和产品关系表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:create')")
    public Object saveCmsPrefrenceAreaProductRelation(@RequestBody CmsPrefrenceAreaProductRelation entity) {
        try {
            if (ICmsPrefrenceAreaProductRelationService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存优选专区和产品关系表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "更新优选专区和产品关系表")
    @ApiOperation("更新优选专区和产品关系表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:update')")
    public Object updateCmsPrefrenceAreaProductRelation(@RequestBody CmsPrefrenceAreaProductRelation entity) {
        try {
            if (ICmsPrefrenceAreaProductRelationService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新优选专区和产品关系表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "删除优选专区和产品关系表")
    @ApiOperation("删除优选专区和产品关系表")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:delete')")
    public Object deleteCmsPrefrenceAreaProductRelation(@ApiParam("优选专区和产品关系表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优选专区和产品关系表id");
            }
            if (ICmsPrefrenceAreaProductRelationService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除优选专区和产品关系表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "给优选专区和产品关系表分配优选专区和产品关系表")
    @ApiOperation("查询优选专区和产品关系表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:read')")
    public Object getCmsPrefrenceAreaProductRelationById(@ApiParam("优选专区和产品关系表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优选专区和产品关系表id");
            }
            CmsPrefrenceAreaProductRelation coupon = ICmsPrefrenceAreaProductRelationService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询优选专区和产品关系表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除优选专区和产品关系表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除优选专区和产品关系表")
    @PreAuthorize("hasAuthority('cms:CmsPrefrenceAreaProductRelation:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ICmsPrefrenceAreaProductRelationService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
