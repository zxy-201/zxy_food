package com.zxy.food.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 艾
 * @Time 2024/5/10 9:55 AM
 */
@Data
public class VarietyDishes {

    @ApiModelProperty("食物id")
    private Long foodId;

    @ApiModelProperty("食物名称")
    private String foodName;
}
