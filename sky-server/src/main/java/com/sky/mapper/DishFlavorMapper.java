package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增风味
     * @param item
     */
    void save(DishFlavor item);

    DishFlavor getDishFlavorByDishId(Long id);
    /**
     * 根据菜品id删除菜品风味数据
     * @param id
     */
    void deleteDishFlavorByDishId(Long id);
}
