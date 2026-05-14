package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.exception.DishNotFoundException;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 包名称： com.sky.service.impl
 * 类名称：DishServiceImpl
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 20:35
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {

    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        DishVO dishVO = dishMapper.getByIdWithFlavor(id);
        if(dishVO == null) {
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        return dishVO;
    }
}
