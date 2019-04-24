package com.zscat.mallplus.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.entity.CmsTopic;
import com.zscat.mallplus.cms.entity.CmsTopicMember;
import com.zscat.mallplus.cms.mapper.CmsTopicMapper;
import com.zscat.mallplus.cms.mapper.CmsTopicMemberMapper;
import com.zscat.mallplus.cms.service.ICmsTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 话题表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class CmsTopicServiceImpl extends ServiceImpl<CmsTopicMapper, CmsTopic> implements ICmsTopicService {

    @Resource
    private CmsTopicMapper topicMapper;
    @Resource
    private CmsTopicMemberMapper topicMemberMapper;

    @Transactional
    @Override
    public int updateVerifyStatus(Long ids,Long topicId, Integer verifyStatus) {
        CmsTopicMember product = new CmsTopicMember();
        product.setStatus(verifyStatus+"");
        product.setId(ids);
        int count = topicMemberMapper.updateById(product);

        CmsTopic topic = topicMapper.selectById(topicId);
        if (verifyStatus==1){
            topic.setAttendCount(topic.getAttendCount()+1);
        }else{
            topic.setAttendCount(topic.getAttendCount()-1);
        }
        topicMapper.updateById(topic);
        return count;
    }
}
