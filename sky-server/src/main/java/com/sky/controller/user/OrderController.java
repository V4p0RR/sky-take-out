package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/order")
@Slf4j
// 订单控制器
public class OrderController {
  @Autowired
  private OrderService orderService;

  /**
   * 用户下单
   * 
   * @param ordersDTO
   * @return
   */
  @PostMapping("/submit")
  public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
    log.info("用户下单:{}", ordersSubmitDTO);
    OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
    return Result.success(orderSubmitVO);
  }

  /**
   * 订单支付
   * 
   * @param ordersPaymentDTO
   * @return
   */
  @PutMapping("/payment")
  public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
    log.info("订单支付:{}", ordersPaymentDTO);
    return Result.success(orderService.payment(ordersPaymentDTO));
  }

  /**
   * 查询订单详情
   * 
   * @param id
   * @return
   */
  @GetMapping("/orderDetail/{id}")
  public Result<OrderVO> orderDetail(@PathVariable Long id) {
    log.info("查询订单详情:id={}", id);
    OrderVO orderVO = orderService.orderDetail(id);
    return Result.success(orderVO);
  }

  /**
   * 取消订单
   * 
   * @param orderId
   * @return
   */
  @PutMapping("/cancel/{id}")
  public Result<Object> cancel(@PathVariable Long id, @RequestBody OrdersCancelDTO ordersCancelDTO) {
    log.info("取消订单:{},{}", id, ordersCancelDTO.getCancelReason());
    ordersCancelDTO.setId(id);
    orderService.cancel(ordersCancelDTO);
    return Result.success();
  }

  /**
   * 历史订单分页查询
   * 
   * @param ordersPageQueryDTO
   * @return
   */
  @GetMapping("/historyOrders")
  public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
    log.info("订单分页查询:{}", ordersPageQueryDTO);
    PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 再来一单
   * 
   * @param id
   * @return
   */
  @PostMapping("/repetition/{id}")
  public Result<Object> repetition(@PathVariable Long id) {
    log.info("再来一单:{}", id);
    orderService.repetition(id);
    return Result.success();
  }

  /**
   * 用户催单
   * 
   * @param id
   * @return
   */
  @GetMapping("/reminder/{id}")
  public Result<Object> reminder(@PathVariable Long id) {
    log.info("用户催单:{}", id);
    orderService.reminder(id);
    return Result.success();
  }

}
