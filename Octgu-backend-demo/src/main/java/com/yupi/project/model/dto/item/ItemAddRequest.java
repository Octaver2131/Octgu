package com.yupi.project.model.dto.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物品添加请求
 *
 * @author yupi
 */
@Data
public class ItemAddRequest {
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
     * 描述
     */
    private String description;
}
