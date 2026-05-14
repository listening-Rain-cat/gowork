package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;

/**
 * 包名称： com.sky.service
 * 类名称：DishService
 * 类描述：菜品业务接口
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 20:35
 */
public interface DishService {
    /**
     * 修改菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);
}