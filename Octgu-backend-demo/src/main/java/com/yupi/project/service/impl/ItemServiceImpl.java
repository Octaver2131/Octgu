package com.yupi.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.ItemMapper;
import com.yupi.project.model.entity.Item;
import com.yupi.project.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
* @author Octaver
* @description 针对表【item(二次元IP周边商品购买信息表（购买时间精确到日）)】的数据库操作Service实现
* @createDate 2025-08-13 18:33:38
*/
@Service
@Slf4j
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item>
    implements ItemService {

    @Override
    public void validItem(Item item, boolean add) {
        if (item == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String itemName = item.getItemName();
        String itemIp = item.getItemIp();
        String itemCategory = item.getItemCategory();
        BigDecimal unitPrice = item.getUnitPrice();
        BigDecimal totalPrice = item.getTotalPrice();
        Integer purchaseNumber = item.getPurchaseNumber();
        Date purchaseTime = item.getPurchaseTime();

        // 创建时，所有必填参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(itemName, itemIp, itemCategory) || 
                ObjectUtils.isNull(unitPrice, totalPrice, purchaseNumber, purchaseTime)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填参数不能为空");
            }
        }

        // 校验名称长度
        if (StringUtils.isNotBlank(itemName) && itemName.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "物品名称过长");
        }

        // 校验IP名称长度
        if (StringUtils.isNotBlank(itemIp) && itemIp.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "IP名称过长");
        }

        // 校验分类长度
        if (StringUtils.isNotBlank(itemCategory) && itemCategory.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称过长");
        }

        // 校验价格
        if (unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "单价不能为负数");
        }

        if (totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总价不能为负数");
        }

        // 校验数量
        if (purchaseNumber != null && purchaseNumber < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "购买数量不能为负数");
        }

        // 校验购买时间
        if (purchaseTime != null && purchaseTime.after(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "购买时间不能为未来时间");
        }
    }
}




