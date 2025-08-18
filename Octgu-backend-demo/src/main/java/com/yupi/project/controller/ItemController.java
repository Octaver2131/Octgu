package com.yupi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.yupi.project.common.BaseResponse;
import com.yupi.project.common.DeleteRequest;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.common.ResultUtils;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.model.dto.item.ItemAddRequest;
import com.yupi.project.model.dto.item.ItemQueryRequest;
import com.yupi.project.model.dto.item.ItemUpdateRequest;
import com.yupi.project.model.entity.Item;
import com.yupi.project.model.vo.ItemVO;
import com.yupi.project.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * 物品控制器
 *
 * @author yupi
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;

    // region 增删改查

    /**
     * 创建物品
     *
     * @param itemAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Integer> addItem(@RequestBody ItemAddRequest itemAddRequest, HttpServletRequest request) {
        if (itemAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemAddRequest, item);
        // 校验
        itemService.validItem(item, true);
        boolean result = itemService.save(item);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(item.getId());
    }

    /**
     * 删除物品
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteItem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = itemService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新物品
     *
     * @param itemUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateItem(@RequestBody ItemUpdateRequest itemUpdateRequest, HttpServletRequest request) {
        if (itemUpdateRequest == null || itemUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemUpdateRequest, item);
        // 校验
        itemService.validItem(item, false);
        boolean result = itemService.updateById(item);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取物品
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ItemVO> getItemById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Item item = itemService.getById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(item, itemVO);
        return ResultUtils.success(itemVO);
    }

    /**
     * 获取物品列表
     *
     * @param itemQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<ItemVO>> listItems(ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        Item itemQuery = new Item();
        if (itemQueryRequest != null) {
            BeanUtils.copyProperties(itemQueryRequest, itemQuery);
        }
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>(itemQuery);
        List<Item> itemList = itemService.list(queryWrapper);
        List<ItemVO> itemVOList = itemList.stream().map(item -> {
            ItemVO itemVO = new ItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(itemVOList);
    }

    /**
     * 分页获取物品列表
     *
     * @param itemQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<ItemVO>> listItemByPage(ItemQueryRequest itemQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        Item itemQuery = new Item();
        if (itemQueryRequest != null) {
            BeanUtils.copyProperties(itemQueryRequest, itemQuery);
            current = itemQueryRequest.getCurrent();
            size = itemQueryRequest.getPageSize();
        }
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>(itemQuery);
        Page<Item> itemPage = itemService.page(new Page<>(current, size), queryWrapper);
        Page<ItemVO> itemVOPage = new PageDTO<>(itemPage.getCurrent(), itemPage.getSize(), itemPage.getTotal());
        List<ItemVO> itemVOList = itemPage.getRecords().stream().map(item -> {
            ItemVO itemVO = new ItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        itemVOPage.setRecords(itemVOList);
        return ResultUtils.success(itemVOPage);
    }

    /**
     * 获取统计信息
     *
     * @param request
     * @return
     */
    @GetMapping("/statistics")
    public BaseResponse<Map<String, Object>> getStatistics(HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        
        // 获取今年的开始时间
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfYear = calendar.getTime();
        
        // 获取去年的开始和结束时间
        calendar.add(Calendar.YEAR, -1);
        Date startOfLastYear = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        Date endOfLastYear = calendar.getTime();

        // 获取全部支出
        BigDecimal totalExpense = itemService.lambdaQuery()
                .select(Item::getTotalPrice)
                .list()
                .stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取本年支出
        BigDecimal yearExpense = itemService.lambdaQuery()
                .select(Item::getTotalPrice)
                .ge(Item::getPurchaseTime, startOfYear)
                .list()
                .stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取去年支出
        BigDecimal lastYearExpense = itemService.lambdaQuery()
                .select(Item::getTotalPrice)
                .ge(Item::getPurchaseTime, startOfLastYear)
                .lt(Item::getPurchaseTime, startOfYear)
                .list()
                .stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取去年总支出
        BigDecimal lastTotalExpense = itemService.lambdaQuery()
                .select(Item::getTotalPrice)
                .lt(Item::getPurchaseTime, startOfYear)
                .list()
                .stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取最喜欢的IP及其数量
        List<Item> items = itemService.list();
        Map<String, Long> ipCountMap = items.stream()
                .collect(Collectors.groupingBy(Item::getItemIp, Collectors.counting()));
        
        String favoriteIp = "";
        Long maxCount = 0L;
        for (Map.Entry<String, Long> entry : ipCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteIp = entry.getKey();
            }
        }

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("totalExpense", totalExpense);
        result.put("yearExpense", yearExpense);
        result.put("lastYearExpense", lastYearExpense);
        result.put("lastTotalExpense", lastTotalExpense);
        result.put("favoriteIp", favoriteIp);
        result.put("favoriteIpCount", maxCount);

        return ResultUtils.success(result);
    }

    /**
     * 获取每月统计信息
     *
     * @param year 指定年份
     * @param request
     * @return
     */
    @GetMapping("/monthlyStatistics")
    public BaseResponse<List<Map<String, Object>>> getMonthlyStatistics(
            @RequestParam(required = false) Integer year, HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        
        // 如果没有指定年份，默认使用当前年份
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        
        // 设置查询开始时间为指定年份的1月1日
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfYear = calendar.getTime();
        
        // 设置查询结束时间为指定年份的12月31日
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfYear = calendar.getTime();
        
        // 获取指定年份的所有物品数据
        List<Item> items = itemService.lambdaQuery()
                .ge(Item::getPurchaseTime, startOfYear)
                .le(Item::getPurchaseTime, endOfYear)
                .list();
        
        // 按月份分组计算每月总支出
        Map<Integer, BigDecimal> monthlyExpenseMap = new HashMap<>();
        
        // 初始化12个月的数据
        for (int i = 1; i <= 12; i++) {
            monthlyExpenseMap.put(i, BigDecimal.ZERO);
        }
        
        // 计算每月支出
        for (Item item : items) {
            Calendar itemCalendar = Calendar.getInstance();
            itemCalendar.setTime(item.getPurchaseTime());
            int month = itemCalendar.get(Calendar.MONTH) + 1; // Calendar.MONTH 从0开始，所以需要+1
            
            BigDecimal currentExpense = monthlyExpenseMap.get(month);
            monthlyExpenseMap.put(month, currentExpense.add(item.getTotalPrice()));
        }
        
        // 构造返回结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", i + "月");
            monthData.put("expense", monthlyExpenseMap.get(i));
            result.add(monthData);
        }
        
        return ResultUtils.success(result);
    }

    // endregion
}