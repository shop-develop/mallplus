package com.zscat.mallplus.ums.mapper;

import com.zscat.mallplus.ums.entity.UmsMemberReceiveAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员收货地址表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface UmsMemberReceiveAddressMapper extends BaseMapper<UmsMemberReceiveAddress> {

    void updateStatusByMember(Long memberId);
}
