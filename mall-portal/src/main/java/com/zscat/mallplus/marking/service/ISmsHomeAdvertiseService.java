package com.zscat.mallplus.marking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.pms.entity.PmsBrand;
import com.zscat.mallplus.pms.entity.PmsProduct;

import java.util.List;

/**
 * <p>
 * 首页轮播广告表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsHomeAdvertiseService extends IService<SmsHomeAdvertise> {

    HomeContentResult singelContent();

     List<PmsBrand> getRecommendBrandList(int pageNum, int pageSize) ;
     List<PmsProduct> getNewProductList(int pageNum, int pageSize) ;
     List<PmsProduct> getHotProductList(int pageNum, int pageSize) ;
     List<CmsSubject> getRecommendSubjectList(int pageNum, int pageSize) ;

     List<SmsHomeAdvertise> getHomeAdvertiseList() ;
}
