package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void update(Orders orders);

    List<Orders> getOrdersByStatus(Integer status);

    Orders getOrdersById(Long id);

    Page<Orders> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
