package com.zscat.mallplus.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 后台用户角色表
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    @TableField(exist = false)
    private  boolean checked =false;

    /**
     * 描述
     */
    private String description;

    /**
     * 后台用户数量
     */
    @TableField("admin_count")
    private Integer adminCount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 启用状态：0->禁用；1->启用
     */
    private Integer status;

    private Integer sort;
    @TableField(exist = false)
    private String menuIds;

}
