package com.sky.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersConfirmDTO;
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

  /**
   * 管理端订单搜索
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  Page<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

  /**
   * 查询各个状态的订单数量统计
   * 
   * @return
   */
  Integer countByStatus(Integer status);

  /**
   * 管理端接单
   * 
   * @param ordersConfirmDTO
   */
  void confirm(OrdersConfirmDTO ordersConfirmDTO);

  /**
   * 管理端派送订单
   * 
   * @param id
   */
  void delivery(Long id);

  /**
   * 订单完成
   * 
   * @param orders
   */
  void complete(Orders orders);

  /**
   * 查询符合时间和状态的订单
   * 
   * @param pendingPayment
   * @param time
   * @return
   */
  List<Orders> getOrdersByStatusAndLTTime(Integer status, LocalDateTime time);
}
