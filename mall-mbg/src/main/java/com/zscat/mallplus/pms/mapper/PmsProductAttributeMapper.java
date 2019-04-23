package com.zscat.mallplus.pms.mapper;

import com.zscat.mallplus.pms.entity.PmsProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.vo.ProductAttrInfo;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
