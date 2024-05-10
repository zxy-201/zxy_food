package com.zxy.food.service;

import com.zxy.food.entity.VarietyDishes;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 艾
 * @Time 2024/5/10 9:58 AM
 */
@Service
public interface FoodService {

    /**
     * 添加菜品
     */
    List<VarietyDishes> addFood(List<VarietyDishes> params);
}
