package com.zscat.mallplus.marking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.service.ICmsSubjectCategoryService;
import com.zscat.mallplus.cms.service.ICmsSubjectCommentService;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.marking.entity.*;
import com.zscat.mallplus.marking.mapper.SmsHomeAdvertiseMapper;
import com.zscat.mallplus.marking.service.*;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.pms.entity.PmsBrand;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.service.IPmsBrandService;
import com.zscat.mallplus.pms.service.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 首页轮播广告表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsHomeAdvertiseServiceImpl extends ServiceImpl<SmsHomeAdvertiseMapper, SmsHomeAdvertise> implements ISmsHomeAdvertiseService {
    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private IOmsOrderService orderService;
    @Resource
    private ISmsGroupService groupService;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private IPmsProductService pmsProductService;
    @Resource
    private IPmsProductAttributeCategoryService productAttributeCategoryService;
    @Resource
    private IPmsProductCategoryService productCategoryService;

    @Resource
    private ISmsHomeBrandService homeBrandService;
    @Resource
    private ISmsHomeNewProductService homeNewProductService;
    @Resource
    private ISmsHomeRecommendProductService homeRecommendProductService;
    @Resource
    private ISmsHomeRecommendSubjectService homeRecommendSubjectService;

    @Resource
    private ICmsSubjectCategoryService subjectCategoryService;
    @Resource
    private ICmsSubjectService subjectService;
    @Resource
    private ICmsSubjectCommentService commentService;
    @Resource
    private IPmsBrandService brandService;
    @Override
    public HomeContentResult singelContent() {
        HomeContentResult result = new HomeContentResult();
        //获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        //获取推荐品牌
        result.setBrandList(this.getRecommendBrandList(0, 4));

        //获取新品推荐
        result.setNewProductList(this.getNewProductList(0, 4));
        //获取人气推荐
        result.setHotProductList(this.getHotProductList(0, 4));
        //获取推荐专题
        result.setSubjectList(this.getRecommendSubjectList(0, 4));
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.list(new QueryWrapper<>());

        for (PmsProductAttributeCategory gt : productAttributeCategoryList) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            productQueryParam.setPublishStatus(1);
            productQueryParam.setVerifyStatus(1);
            List<PmsProduct> goodsList = pmsProductService.list(new QueryWrapper<>(productQueryParam));
            if (goodsList!=null && goodsList.size()>0){
                PmsProduct pmsProduct = goodsList.get(0);
                PmsProduct product =  new PmsProduct();
                BeanUtils.copyProperties(pmsProduct, product);
              //  product.setType(1);
                goodsList.add(product);
            }
            gt.setGoodsList(goodsList);
        }
        result.setCat_list(productAttributeCategoryList);
        return result;
    }
    @Override
    public List<PmsBrand> getRecommendBrandList(int pageNum, int pageSize) {
        List<SmsHomeBrand> brands = homeBrandService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsBrand>) brandService.listByIds(brands);

    }
    @Override
    public List<PmsProduct> getNewProductList(int pageNum, int pageSize) {
        List<SmsHomeNewProduct> brands = homeNewProductService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsProduct>) pmsProductService.listByIds(brands);
    }
    @Override
    public List<PmsProduct> getHotProductList(int pageNum, int pageSize) {
        List<SmsHomeRecommendProduct> brands = homeRecommendProductService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<PmsProduct>) pmsProductService.listByIds(brands);
    }
    @Override
    public List<CmsSubject> getRecommendSubjectList(int pageNum, int pageSize) {
        List<SmsHomeRecommendSubject> brands = homeRecommendSubjectService.list(new QueryWrapper<>());
        List<Long> ids = new ArrayList<>();
        return (List<CmsSubject>) subjectService.listByIds(brands);
    }
    @Override
    public List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertise advertise = new SmsHomeAdvertise();
        advertise.setStatus(1);
        return  advertiseService.list(new QueryWrapper<>(advertise));
    }


}
