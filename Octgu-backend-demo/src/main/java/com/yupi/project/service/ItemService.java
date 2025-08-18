package com.yupi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.project.model.entity.Item;


/**
* @author Octaver
* @description 针对表【item(二次元IP周边商品购买信息表（购买时间精确到日）)】的数据库操作Service
* @createDate 2025-08-13 18:33:38
*/
public interface ItemService extends IService<Item> {

    /**
     * 校验物品信息
     *
     * @param item 物品实体
     * @param add 是否为创建校验
     */
    void validItem(Item item, boolean add);
}
