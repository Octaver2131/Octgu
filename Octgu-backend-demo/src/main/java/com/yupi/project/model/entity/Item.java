package com.yupi.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 二次元IP周边商品购买信息表（购买时间精确到日）
 * @TableName item
 */
@TableName(value ="item")
@Data
public class Item implements Serializable {
    /**
     * 自增主键，唯一标识每条记录
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * item名字（如周边商品名称）
     */
    private String itemName;

    /**
     * item对应的二次元IP名称（如动漫、游戏、影视作品名）
     */
    private String itemIp;

    /**
     * 描述
     */
    private String description;

    /**
     * item种类（如徽章、手办、立牌等）
     */
    private String itemCategory;

    /**
     * 单价（保留2位小数）
     */
    private BigDecimal unitPrice;

    /**
     * 总价（保留2位小数）
     */
    private BigDecimal totalPrice;

    /**
     * 购买数量（非负整数）
     */
    private Integer purchaseNumber;

    /**
     * 购买时间（仅到日，格式：YYYY-MM-DD）
     */
    private Date purchaseTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}