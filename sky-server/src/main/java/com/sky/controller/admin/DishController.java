package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包名称： com.sky.controller.admin
 * 类名称：DishController
 * 类描述：菜品管理相关接口
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 19:30
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    /**
     * 修改菜品
     */
    @PutMapping("")
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }
    /**
     * 批量删除菜品
     */
    @DeleteMapping("")
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteList(ids);
        return Result.success();
    }
    /**
     * 新增菜品
     */
    @PostMapping("")
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }
    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<List<DishVO>> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        List<DishVO> dishVO = dishService.getByIdWithFlavor(id);
        System.out.println(dishVO);
        return Result.success(dishVO);
    }
    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> getDishByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        List<DishVO> list = dishService.getDishByCategoryId(categoryId);
        return Result.success(list);
    }
    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 菜品起售停售
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable(value = "status") Integer status,Long id){
        log.info("菜品起售停售：{}", status);
        dishService.startOrStop(status,id);
        return Result.success();
    }
}