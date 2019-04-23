package com.zscat.mallplus.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.OmsMoneyInfoParam;
import com.zscat.mallplus.oms.vo.OmsOrderDeliveryParam;
import com.zscat.mallplus.oms.vo.OmsReceiverInfoParam;
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
 * 订单表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "OmsOrderController", description = "订单表管理")
@RequestMapping("/oms/OmsOrder")
public class OmsOrderController {
    @Resource
    private IOmsOrderService IOmsOrderService;

    @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单表列表")
    @ApiOperation("根据条件查询所有订单表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('oms:OmsOrder:read')")
    public Object getOmsOrderByPage(OmsOrder entity,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IOmsOrderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有订单表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "保存订单表")
    @ApiOperation("保存订单表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('oms:OmsOrder:create')")
    public Object saveOmsOrder(@RequestBody OmsOrder entity) {
        try {
            if (IOmsOrderService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存订单表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "更新订单表")
    @ApiOperation("更新订单表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrder:update')")
    public Object updateOmsOrder(@RequestBody OmsOrder entity) {
        try {
            if (IOmsOrderService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "删除订单表")
    @ApiOperation("删除订单表")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
    public Object deleteOmsOrder(@ApiParam("订单表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单表id");
            }
            if (IOmsOrderService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除订单表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "给订单表分配订单表")
    @ApiOperation("查询订单表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrder:read')")
    public Object getOmsOrderById(@ApiParam("订单表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单表id");
            }
            OmsOrder coupon = IOmsOrderService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询订单表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除订单表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除订单表")
    @PreAuthorize("hasAuthority('oms:OmsOrder:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IOmsOrderService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "oms", REMARK = "批量发货")
    @ApiOperation("批量发货")
    @RequestMapping(value = "/update/delivery", method = RequestMethod.POST)
    @ResponseBody
    public Object delivery(@RequestBody List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = IOmsOrderService.delivery(deliveryParamList);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "批量关闭订单")
    @ApiOperation("批量关闭订单")
    @RequestMapping(value = "/update/close", method = RequestMethod.POST)
    @ResponseBody
    public Object close(@RequestParam("ids") List<Long> ids, @RequestParam String note) {
        int count = IOmsOrderService.close(ids, note);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "oms", REMARK = "修改收货人信息")
    @ApiOperation("修改收货人信息")
    @RequestMapping(value = "/update/receiverInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object updateReceiverInfo(@RequestBody OmsReceiverInfoParam receiverInfoParam) {
        int count = IOmsOrderService.updateReceiverInfo(receiverInfoParam);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "修改订单费用信息")
    @ApiOperation("修改订单费用信息")
    @RequestMapping(value = "/update/moneyInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object updateReceiverInfo(@RequestBody OmsMoneyInfoParam moneyInfoParam) {
        int count = IOmsOrderService.updateMoneyInfo(moneyInfoParam);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "备注订单")
    @ApiOperation("备注订单")
    @RequestMapping(value = "/update/note", method = RequestMethod.POST)
    @ResponseBody
    public Object updateNote(@RequestParam("id") Long id,
                             @RequestParam("note") String note,
                             @RequestParam("status") Integer status) {
        int count = IOmsOrderService.updateNote(id, note, status);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
}
