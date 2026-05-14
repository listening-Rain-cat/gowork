package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Category getById(Long id);

    /**
     * 修改分类
     * @param category
     */
    void update(Category category);
    /**
     * 分类分页查询
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    /**
     * 根据名字查询分类
     * @param name
     * @return
     */
    Category getByName(String name);

    /**
     * 删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    Category getByType(Integer type);

    /**
     * 新增分类
     * @param category
     */
    void save(Category category);
    /**
     * 查找当前最大
     */
    Integer getMaxSort();

    /**
     * 根据类型查询分类列表
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
