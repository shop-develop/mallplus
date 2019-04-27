package com.zscat.mallplus.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.bo.Tree;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysPermissionNode;
import com.zscat.mallplus.sys.mapper.SysPermissionMapper;
import com.zscat.mallplus.sys.service.ISysPermissionService;
import com.zscat.mallplus.sys.service.ISysUserService;
import com.zscat.mallplus.util.BuildTree;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户权限表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper permissionMapper;
    @Resource
    private ISysUserService userService;
    @Override
    public List<Tree<SysPermission>> getPermissionsByUserId(Long id) {
        List<Tree<SysPermission>> trees = new ArrayList<Tree<SysPermission>>();
      //  List<SysPermission> menuDOs = permissionMapper.listMenuByUserId(id);
        List<SysPermission> menuDOs = userService.listUserPerms(id);
        for (SysPermission sysMenuDO : menuDOs) {
            Tree<SysPermission> tree = new Tree<SysPermission>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getPid().toString());
            tree.setTitle(sysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysMenuDO.getUri());
            attributes.put("icon", sysMenuDO.getIcon());
            tree.setMeta(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<SysPermission>> list = BuildTree.buildList(trees, "0");
        return list;
    }

    @Override
    public List<SysPermissionNode> treeList() {
        List<SysPermission> permissionList = permissionMapper.selectList(new QueryWrapper<>());
        List<SysPermissionNode> result = permissionList.stream()
                .filter(permission -> permission.getPid().equals(0L))
                .map(permission -> covert(permission, permissionList)).collect(Collectors.toList());
        return result;
    }
    /**
     * 将权限转换为带有子级的权限对象
     * 当找不到子级权限的时候map操作不会再递归调用covert
     */
    private SysPermissionNode covert(SysPermission permission, List<SysPermission> permissionList) {
        SysPermissionNode node = new SysPermissionNode();
        BeanUtils.copyProperties(permission, node);
        List<SysPermissionNode> children = permissionList.stream()
                .filter(subPermission -> subPermission.getPid().equals(permission.getId()))
                .map(subPermission -> covert(subPermission, permissionList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
