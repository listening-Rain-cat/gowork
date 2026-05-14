package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包名称： com.sky.controller.admin
 * 类名称：CategoryController
 * 类描述：分类管理相关接口
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 15:52
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "菜品分类相关接口")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    /**
     *修改分类
     */
    @PutMapping("")
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody @Validated CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
    /**
     * 分类分页查询
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(@Validated CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult) ;
    }

    /**
     * 启用禁用分类
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable Integer status,@RequestParam(value = "id") Long id) {
        log.info("启用禁用分类：id={}, status={}", id, status);
        categoryService.startOrStop(status, id);
        return Result.success();
    }
    /**
     * 新增分类
     */
    @PostMapping("")
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody @Validated CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }
    /**
     * 根据id删除分类
     */
    @DeleteMapping("")
    @ApiOperation("根据id删除分类")
    public Result<String> deleteById(@RequestParam(value = "id") Long id) {
        if(id == null) {
            return Result.error("分类id不能为空");
        }
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(@RequestParam(value = "type") Integer type) {
        if(type == null) {
            return Result.error("分类类型不能为空");
        }
        List<Category> category = categoryService.list(type);
        return Result.success(category);
    }
}