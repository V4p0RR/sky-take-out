package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

  /**
   * 用户下单
   * 
   * @param ordersDTO
   * @return
   */
  OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

  /**
   * 订单支付
   * 
   * @param ordersPaymentDTO
   * @return
   */
  OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

  /**
   * 查询订单详情
   * 
   * @param id
   * @return
   */
  OrderVO orderDetail(Long id);

  /**
   * 取消订单
   * 
   * @param orderId
   */
  void cancel(OrdersCancelDTO ordersCancelDTO);

  /**
   * 历史订单分页查询
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

  /**
   * 再来一单
   * 
   * @param id
   */
  void repetition(Long id);

  /**
   * 管理端订单搜索
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

  /**
   * 管理端查询订单详情
   * 
   * @param id
   * @return
   */
  OrderVO details(Long id);

  /**
   * 查询各个状态的订单数量统计
   * 
   * @return
   */
  OrderStatisticsVO statistics();

  /**
   * 管理端取消订单
   * 
   * @param ordersCancelDTO
   */
  void rejection(OrdersRejectionDTO ordersRejectionDTO);

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
   * 管理端完成订单
   * 
   * @param id
   */
  void complete(Long id);

  /**
   * 用户催单
   * 
   * @param id
   */
  void reminder(Long id);

}
