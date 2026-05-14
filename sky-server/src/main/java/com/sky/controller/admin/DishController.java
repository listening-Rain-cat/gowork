package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result update(DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }
    /**
     * 批量删除菜品
     */

    /**
     * 新增菜品
     */

    /**
     * 根据id查询菜品
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        System.out.println(dishVO);
        return Result.success(dishVO);
    }
    /**
     * 根据分类id查询菜品
     */

    /**
     * 菜品分页查询
     */

    /**
     * 菜品起售停售
     */
}