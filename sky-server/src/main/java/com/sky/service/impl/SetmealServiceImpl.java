package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名称： com.sky.service.impl
 * 类名称：SetmealServiceImpl
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-16 19:32
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐
     * 业务规则：
     * <p>
     * - 套餐名称唯一
     * - 套餐必须属于某个分类
     * - 套餐必须包含菜品
     * - 名称、分类、价格、图片为必填项
     * - 添加菜品窗口需要根据分类类型来展示菜品
     * - 新增的套餐默认为停售状态
     *
     * @param setmealDTO
     */
    @Override
    public void save(SetmealDTO setmealDTO) {
        //根据套餐名称获取套餐，名称唯一
        if(setmealMapper.getByName(setmealDTO.getName()) != null){
            throw  new BaseException(MessageConstant.SETMEAL_NAME_EXISTS);
        }
        //获取套餐的分类并且判断该分类是否合法
        if(categoryMapper.getById(setmealDTO.getCategoryId()) == null){
            throw new BaseException(MessageConstant.CATEGORY_NOT_FOUND);
        }
        //获取套餐的菜品是否为空
        if(setmealDTO.getSetmealDishes().isEmpty()){
            throw new BaseException(MessageConstant.SETMEAL_NOT_HAVE_DISHES);
        }
        //判断菜品是否合法
        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            if(dishMapper.selectDishById(setmealDish.getDishId()) == null){
                throw new BaseException(MessageConstant.DISH_NOT_FOUND);
            }
        }
        //业务逻辑
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmeal.setStatus(0);
        setmealMapper.save(setmeal);
        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.save(setmealDish);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> pages = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(pages.getTotal(),pages.getResult());

    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal,setmealVO);
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 批量删除
     * 业务规则：
     *
     * - 可以一次删除一个套餐，也可以批量删除套餐
     * - 起售中的套餐不能删除
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
        for(Long id : ids){
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == 1){
                throw  new BaseException(MessageConstant.DISH_ON_SALE);
            }
        }
        setmealMapper.deleteListById(ids);
        setmealDishMapper.deleteListById(ids);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for(SetmealDish setmealDish : setmealDishes){
            setmealDishMapper.updateById(setmealDish);
        }
    }

    /**
     * 套餐的起售和停售
     * 业务规则：
     * - 可以对状态为起售的套餐进行停售操作，可以对状态为停售的套餐进行起售操作
     * - 起售的套餐可以展示在用户端，停售的套餐不能展示在用户端
     * - 起售套餐时，如果套餐内包含停售的菜品，则不能起售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(@PathVariable Integer status, Long id) {
        //获取该套餐下的菜品id
        List<SetmealDish> setmealDishes = setmealDishMapper.getDishByDishId(id);
        List<Dish> dishes = new ArrayList<>();
        for(SetmealDish setmealDish : setmealDishes){
            dishes.add(dishMapper.getDishById(setmealDish.getDishId()));
        }
        for (Dish dish : dishes){
            if(dish.getStatus() == 0){
                throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        //对套餐进行状态修改
        setmealMapper.update(setmeal);
    }
}