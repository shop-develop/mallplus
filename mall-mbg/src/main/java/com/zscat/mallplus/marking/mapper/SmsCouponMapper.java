package com.zscat.mallplus.marking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.marking.entity.SmsCoupon;
import com.zscat.mallplus.marking.vo.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠卷表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {

    List<SmsCoupon> selectNotRecive(Long memberId);

    List<SmsCoupon> selectRecive(Long memberId);

    SmsCouponParam getItem(@Param("id") Long id);
}
