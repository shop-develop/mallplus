package com.zscat.mallplus.pms.mapper;

import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.vo.PmsProductAttributeCategoryItem;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductAttributeCategoryMapper extends BaseMapper<PmsProductAttributeCategory> {

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
