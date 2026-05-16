package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
     List<SetmealDish> getSetmealDishByDishId(Long id);

    void save(SetmealDish setmealDish);

    List<SetmealDish> getSetmealDishBySetmealId(Long id);

    void deleteListById(List<Long> ids);

    void updateById(SetmealDish setmealDish);

    List<SetmealDish> getDishByDishId(Long id);
}
