package com.yupi.project.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yupi.project.model.entity.Item;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物品视图对象
 *
 * @author yupi
 */
@Data
public class ItemVO {
    /**
     * 自增主键，唯一标识每条记录
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 从Item实体类转换为ItemVO
     *
     * @param item
     * @return
     */
    public static ItemVO objToVo(Item item) {
        if (item == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        // 复制基本属性
        itemVO.setId(item.getId());
        itemVO.setItemName(item.getItemName());
        itemVO.setItemIp(item.getItemIp());
        itemVO.setItemCategory(item.getItemCategory());
        itemVO.setUnitPrice(item.getUnitPrice());
        itemVO.setTotalPrice(item.getTotalPrice());
        itemVO.setPurchaseNumber(item.getPurchaseNumber());
        itemVO.setPurchaseTime(item.getPurchaseTime());
        itemVO.setCreateTime(item.getCreateTime());
        itemVO.setUpdateTime(item.getUpdateTime());
        itemVO.setDescription(item.getDescription());
        return itemVO;
    }
}