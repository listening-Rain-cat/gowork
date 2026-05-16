package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DishAlreadyExistException;
import com.sky.exception.DishNotFoundException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名称： com.sky.service.impl
 * 类名称：DishServiceImpl
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 20:35
 */
@Service
public class DishServiceImpl implements DishService {
    private static final Logger log = LoggerFactory.getLogger(DishServiceImpl.class);
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        //若菜品不存在，抛出异常
        if(dishMapper.selectDishById(dishDTO.getId()) == null) {
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        
        // 1. 更新菜品基本信息（只更新传入的非空字段）
        dishMapper.updateDishWithFlavorsById(dishDTO);
        
        // 2. 如果传入了口味数据，先删除旧口味，再添加新口味
        if(dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty()) {
            // 删除该菜品的所有旧口味
            dishFlavorMapper.deleteDishFlavorByDishId(dishDTO.getId());
            
            // 添加新口味
            List<DishFlavor> dishFlavors = dishDTO.getFlavors();
            for(DishFlavor flavor : dishFlavors){
                flavor.setDishId(dishDTO.getId());
                dishFlavorMapper.save(flavor);
            }
        }
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public List<DishVO> getByIdWithFlavor(Long id) {
        List<DishVO> dishVO = dishMapper.getByIdWithFlavor(id);
        if(dishVO == null) {
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        return dishVO;
    }

    /**
     * 批量删除菜品
     * 业务规则：
     *     可以一次删除一个菜品，也可以批量删除菜品
     *     起售中的菜品不能删除
     *     被套餐关联的菜品不能删除
     *     删除菜品后，关联的口味数据也需要删除掉
     * @param ids
     */
    @Override
    public void deleteList(List<Long> ids) {
        List<Dish> lists = new ArrayList<>();
        for(Long id : ids){
            Dish dish = dishMapper.getDishById(id);
            lists.add(dish);
        }
        for(Dish item : lists){
            //若当前的菜品的状态为起售，则不能被删除
            if(item.getStatus() == 1){
                throw  new BaseException(MessageConstant.DISH_ON_SALE);
            }
            //获取包含当前菜品的所有套餐
            List<SetmealDish> mealLists = setmealDishMapper.getSetmealDishByDishId(item.getId());
            //如果包含了则抛菜品包含套餐不可删除异常
            if(!mealLists.isEmpty()){
                throw  new BaseException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            //删除该菜品
            dishMapper.deleteListById(ids);
            //与此菜品关联的口味也要删除
//            DishFlavor dishFlavor = dishFlavorMapper.getDishFlavorByDishId(item.getId());
            dishFlavorMapper.deleteDishFlavorByDishId(item.getId());
        }

    }

    /**
     * 分页查询
     * 业务规则：
     *     根据页码展示菜品信息
     *     每页展示10条数据
     *     分页查询时可以根据需要输入菜品名称、菜品分类、菜品状态进行查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<Dish> pages = dishMapper.getPage(dishPageQueryDTO);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.updateDishById(dish);
    }

    @Override
    public List<DishVO> getDishByCategoryId(Long categoryId) {
        List<DishVO> list = dishMapper.getDishAndFlavorByCategoryId(categoryId);
        return list;
    }

    /**
     * 新增菜品
     * 业务规则：
     *     菜品名称必须是唯一的
     *     菜品必须属于某个分类下，不能单独存在
     *     新增菜品时可以根据情况选择菜品的口味
     *     每个菜品必须对应一张图片
     * @param dishDTO
     */
    @Override
    public void save(DishDTO dishDTO) {
        //菜品名称具有唯一性
        if(dishMapper.getDishByName(dishDTO.getName()) != null){
            throw new DishAlreadyExistException(MessageConstant.DISH_EXISTED);
        }
        //菜品必须属于某个分类下
        if(dishDTO.getCategoryId() == null){
            throw new BaseException(MessageConstant.DISH_NOT_HAVE_CATEGORY);
        }
        //菜品必须有图片
        if(dishDTO.getImage() == null || dishDTO.getImage().isEmpty()){
            throw  new BaseException(MessageConstant.DISH_NOT_HAVE_IMAGE);
        }
        //先添加菜品，对菜品表操作
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.save(dish);
        //再添加该菜品的口味
        List<DishFlavor> dishFlavors;
        dishFlavors = dishDTO.getFlavors();
        for(DishFlavor item: dishFlavors){
            item.setDishId(dish.getId());
            dishFlavorMapper.save(item);
        }
    }
}
