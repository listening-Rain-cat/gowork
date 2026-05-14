package com.sky.mapper;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
     DishVO getByIdWithFlavor(Long id);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 根据id修改菜品
     * @param dishDTO
     */
     void updateById(DishDTO dishDTO);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    Dish selectDishById(Long id);

    /**
     * 根据菜品id查询口味信息
     * @param id
     * @return
     */
    DishFlavor selectDishFlavorByDishId(Long id);
}
