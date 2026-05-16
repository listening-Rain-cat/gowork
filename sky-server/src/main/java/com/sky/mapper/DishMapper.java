package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */

     List<DishVO> getByIdWithFlavor(Long id);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 根据id修改菜品与风味
     * @param dishDTO
     */
//    @AutoFill(value = OperationType.UPDATE)
     void updateDishWithFlavorsById(DishDTO dishDTO);

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
    /**
     * 根据id列表删除
     */
    int deleteListById(List<Long> ids);

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<Dish> getPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id修改菜品
     * @param dish
     */
    void updateDishById(Dish dish);
    /**
     * 根据分类id获取菜品和口味
     */
    List<DishVO> getDishAndFlavorByCategoryId(Long categoryId);

    /**
     * 通过菜品名称查询菜品
     * @param name
     * @return
     */
    Dish getDishByName(String name);

    /**
     * 新增菜品
     * @param dish
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    Long save(Dish dish);

    /**
     * 通过id查询菜品
     * @param id
     * @return
     */
    Dish getDishById(Long id);
}
