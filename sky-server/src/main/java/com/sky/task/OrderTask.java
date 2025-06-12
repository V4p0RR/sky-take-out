package com.sky.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sky.dto.OrdersCancelDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {
  @Autowired
  private OrderMapper orderMapper;

  /**
   * 定时处理超时订单
   * 每分钟执行一次
   */
  @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行一次
  public void processTimeOutOrder() {
    log.info("定时处理超时订单:{}", LocalDateTime.now());
    LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
    List<Orders> timeOutOrders = orderMapper.getOrdersByStatusAndLTTime(Orders.PENDING_PAYMENT, time);
    // 如果订单状态是待付款，且超过15分钟未支付，则自动取消订单
    if (timeOutOrders != null && timeOutOrders.size() > 0) {
      for (Orders order : timeOutOrders) {
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason("订单超时未支付，已自动取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.cancel(order);
      }
    }
  }

  /**
   * 处理配送订单
   * 每天凌晨一点执行一次
   */
  @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
  public void processDeliveryOrder() {
    log.info("定时处理配送中订单:{}", LocalDateTime.now());
    LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
    // 查询配送中订单
    List<Orders> deliveryOrders = orderMapper.getOrdersByStatusAndLTTime(Orders.DELIVERY_IN_PROGRESS, time);
    if (deliveryOrders != null && deliveryOrders.size() > 0) {
      for (Orders order : deliveryOrders) {
        order.setStatus(Orders.COMPLETED);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.payment(order);
        log.info("订单号:{}已自动完成配送", order.getNumber());
      }
    }
  }
}
