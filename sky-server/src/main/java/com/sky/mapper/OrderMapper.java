package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;

@Mapper
public interface OrderMapper {

  /**
   * 插入订单
   * 
   * @param orders
   */
  void insert(Orders orders);

  /**
   * 订单支付
   * 
   * @param orders
   */
  void payment(Orders orders);

  /**
   * 根据id查询订单
   * 
   * @param id
   * @return
   */
  Orders getById(Long id);

  /**
   * 取消订单
   * 
   * @param cancelled
   */
  void cancel(Orders orders);

  /**
   * 历史订单分页查询
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  Page<OrderVO> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);
}
