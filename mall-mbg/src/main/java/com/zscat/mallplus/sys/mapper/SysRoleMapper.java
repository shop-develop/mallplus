package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 获取用于所有角色
     */
    List<SysRole> getRoleListByUserId(@Param("adminId") Long adminId);
}
