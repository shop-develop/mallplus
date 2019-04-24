package com.zscat.mallplus.sys.service;

import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysRolePermission;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface ISysRoleService extends IService<SysRole> {
    List<SysRolePermission> getRolePermission(Long roleId);

    /**
     * 获取指定角色权限
     */
    List<SysPermission> getPermissionList(Long roleId);

    boolean saves(SysRole entity);

    boolean updates(SysRole entity);
}
