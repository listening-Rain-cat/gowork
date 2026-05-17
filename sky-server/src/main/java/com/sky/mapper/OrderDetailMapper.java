package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 包名称： com.sky.mapper
 * 类名称：OrderDetailMapper
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-17 01:20
 */
@Mapper
public interface OrderDetailMapper {
    List<OrderDetail> getByOrderId(Long id);
}