package com.zscat.mallplus.marking.service;

import com.zscat.mallplus.marking.entity.SmsRedPacket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 红包 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsRedPacketService extends IService<SmsRedPacket> {
    int acceptRedPacket(Integer id);

    int createRedPacket(SmsRedPacket redPacket);
}
