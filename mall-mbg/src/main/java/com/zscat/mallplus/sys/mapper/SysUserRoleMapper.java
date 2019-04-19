package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysRole;
import com.zscat.mallplus.sys.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {


    List<SysRole> getRoleListByUserid(Long adminId);
}
