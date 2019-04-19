package com.zscat.mallplus.marking.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 红包
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("sms_red_packet")
public class SmsRedPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 红包编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发红包的用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 红包金额
     */
    private BigDecimal amount;

    /**
     * 发红包日期
     */
    @TableField("send_date")
    private LocalDate sendDate;

    /**
     * 红包总数
     */
    private Integer total;

    /**
     * 单个红包的金额
     */
    @TableField("unit_amount")
    private BigDecimal unitAmount;

    /**
     * 红包剩余个数
     */
    private Integer stock;

    /**
     * 红包类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String note;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "SmsRedPacket{" +
        ", id=" + id +
        ", userId=" + userId +
        ", amount=" + amount +
        ", sendDate=" + sendDate +
        ", total=" + total +
        ", unitAmount=" + unitAmount +
        ", stock=" + stock +
        ", type=" + type +
        ", note=" + note +
        "}";
    }
}
