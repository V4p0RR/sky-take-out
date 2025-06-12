package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@RestController("adminDishController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController { // 管理端订单控制器
  @Autowired
  private OrderService orderService;

  /**
   * 管理端订单搜索
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  @GetMapping("/conditionSearch")
  public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
    log.info("管理端订单搜索:{}", ordersPageQueryDTO);
    PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 管理端查询订单详情
   * 
   * @param id
   * @return
   */
  @GetMapping("/details/{id}")
  public Result<OrderVO> details(@PathVariable Long id) {
    log.info("管理端查询订单详情:{}", id);
    OrderVO orderVO = orderService.details(id);
    return Result.success(orderVO);
  }

  /**
   * 查询各个状态的订单数量统计
   * 
   * @return
   */
  @GetMapping("/statistics")
  public Result<OrderStatisticsVO> statistics() {
    log.info("查询各个状态的订单数量统计");
    OrderStatisticsVO orderStatisticsVO = orderService.statistics();
    return Result.success(orderStatisticsVO);
  }

  /**
   * 管理端取消订单
   * 
   * @param ordersCancelDTO
   * @return
   */
  @PutMapping("/cancel")
  public Result<Object> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
    log.info("管理端取消订单:{}", ordersCancelDTO);
    orderService.cancel(ordersCancelDTO);
    return Result.success();
  }

  /**
   * 管理端拒绝订单
   * 
   * @param ordersCancelDTO
   * @return
   */
  @PutMapping("/rejection")
  public Result<Object> rejection(@RequestBody OrdersRejectionDTO ordersrRejectionDTO) {
    log.info("管理端拒绝订单");
    orderService.rejection(ordersrRejectionDTO);
    return Result.success();
  }

  /**
   * 管理端接单
   * 
   * @param ordersConfirmDTO
   */
  @PutMapping("/confirm")
  public Result<Object> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
    log.info("管理端接单:{}", ordersConfirmDTO);
    ordersConfirmDTO.setStatus(Orders.CONFIRMED);
    orderService.confirm(ordersConfirmDTO);
    return Result.success();
  }

  /**
   * 派送订单
   * 
   * @param id
   * @return
   */
  @PutMapping("/delivery/{id}")
  public Result<Object> delivery(@PathVariable Long id) {
    log.info("派送订单:{}", id);
    orderService.delivery(id);
    return Result.success();
  }

  /**
   * 订单完成
   * 
   * @param id
   * @return
   */
  @PutMapping("/complete/{id}")
  public Result<Object> complete(@PathVariable Long id) {
    log.info("订单完成:{}", id);
    orderService.complete(id);
    return Result.success();
  }
}
