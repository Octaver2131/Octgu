package com.yupi.project.model.dto.item;

import com.yupi.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物品查询请求
 *
 * @author yupi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemQueryRequest extends PageRequest {
    /**
     * 主键
     */
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
     * 购买时间（开始）
     */
    private Date purchaseTimeStart;

    /**
     * 购买时间（结束）
     */
    private Date purchaseTimeEnd;
}