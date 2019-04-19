package com.zscat.mallplus.marking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 限时购场次表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("sms_flash_promotion_session")
public class SmsFlashPromotionSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场次名称
     */
    private String name;

    /**
     * 每日开始时间
     */
    @TableField("start_time")
    private LocalTime startTime;

    /**
     * 每日结束时间
     */
    @TableField("end_time")
    private LocalTime endTime;

    /**
     * 启用状态：0->不启用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SmsFlashPromotionSession{" +
        ", id=" + id +
        ", name=" + name +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", status=" + status +
        ", createTime=" + createTime +
        "}";
    }
}
