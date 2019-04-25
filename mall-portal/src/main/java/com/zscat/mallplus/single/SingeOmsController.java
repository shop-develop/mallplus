package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.marking.service.ISmsGroupService;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.GroupAndOrderVo;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@RestController
@Api(tags = "OmsController", description = "订单管理系统")
@RequestMapping("/api/single/oms")
public class SingeOmsController extends ApiBaseAction {

    @Resource
    private ISmsGroupService groupService;
    @Resource
    private IOmsOrderService orderService;


    @IgnoreAuth
    @SysLog(MODULE = "oms", REMARK = "查询订单列表")
    @ApiOperation(value = "查询订单列表")
    @GetMapping(value = "/order/list")
    public Object orderList(OmsOrder order,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(orderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<>(order)));
    }


    /**
     * 提交订单
     * @param orderParam
     * @return
     */
    @ApiOperation("商品详情预览订单")
    @SysLog(MODULE = "order", REMARK = "商品详情预览订单")
    @GetMapping(value = "/preOrder")
    public Object preOrder(GroupAndOrderVo orderParam) {
        UmsMember member = this.getCurrentMember();
        orderParam.setMemberId(member.getId());
        orderParam.setName(member.getNickname());
        return orderService.preSingelOrder(orderParam);
    }
    /**
     * 提交订单
     * @param orderParam
     * @return
     */
    @ApiOperation("商品详情生成订单")
    @SysLog(MODULE = "order", REMARK = "商品详情生成订单")
    @PostMapping(value = "/bookOrder")
    public Object bookOrder(GroupAndOrderVo orderParam) {
        UmsMember member = this.getCurrentMember();
        return orderService.generateSingleOrder(orderParam,member);
    }
}
