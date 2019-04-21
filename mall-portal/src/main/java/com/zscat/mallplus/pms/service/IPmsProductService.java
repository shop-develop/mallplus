package com.zscat.mallplus.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.vo.PmsProductAndGroup;
import com.zscat.mallplus.pms.vo.PmsProductResult;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IPmsProductService extends IService<PmsProduct> {

    PmsProductAndGroup getProductAndGroup(Long id);

    PmsProductResult getUpdateInfo(Long id);
}
