package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.OrderPaymentVO;
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

}
