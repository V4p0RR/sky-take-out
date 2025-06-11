package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private AddressBookMapper addressBookMapper;
  @Autowired
  private OrderDetailMapper orderDetailMapper;
  @Autowired
  private ShoppingCartMapper shoppingCartMapper;

  /**
   * 用户下单
   * 
   * @param ordersDTO
   * @return
   */
  @Transactional
  public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
    if (addressBookMapper.getById(ordersSubmitDTO.getAddressBookId()) == null) {
      throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
    } // 如果地址为空 不能下单
    if (shoppingCartMapper.list(BaseContext.getCurrentId()).size() == 0) {
      throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
    } // 如果购物车为空，不能下单
    // 封装Orders
    AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
    String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName()
        + addressBook.getDetail();
    Orders orders = Orders.builder()
        .number(String.valueOf(System.currentTimeMillis()))
        .userId(BaseContext.getCurrentId())
        .orderTime(LocalDateTime.now())
        .address(address)
        .phone(addressBookMapper.getById(ordersSubmitDTO.getAddressBookId()).getPhone())
        .consignee(addressBookMapper.getById(ordersSubmitDTO.getAddressBookId()).getConsignee())
        .payStatus(Orders.UN_PAID)
        .status(Orders.PENDING_PAYMENT)
        .build();
    BeanUtils.copyProperties(ordersSubmitDTO, orders);
    // 插入orders 要主键返回
    orderMapper.insert(orders);
    // 插入orderdetail
    List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(orders.getUserId());
    List<OrderDetail> orderDetails = new ArrayList<>();
    for (ShoppingCart cart : shoppingCarts) {
      OrderDetail orderDetail = new OrderDetail();
      BeanUtils.copyProperties(cart, orderDetail);
      orderDetail.setOrderId(orders.getId());
      orderDetails.add(orderDetail); // 循环插入orderdetails list
    }
    orderDetailMapper.insert(orderDetails); // 插入orderdetail表

    // 删除购物车
    shoppingCartMapper.clean(orders.getUserId());
    // 封装返回结果
    OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
        .id(orders.getId())
        .orderNumber(orders.getNumber())
        .orderAmount(orders.getAmount())
        .orderTime(orders.getOrderTime())
        .build();
    return orderSubmitVO;
  }

  /**
   * 订单支付
   * 
   * @param
   * @return
   */
  public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
    Orders orders = Orders.builder()
        .status(Orders.TO_BE_CONFIRMED)
        .checkoutTime(LocalDateTime.now())
        .payStatus(Orders.PAID)
        .number(ordersPaymentDTO.getOrderNumber())
        .build();
    orderMapper.payment(orders);
    return new OrderPaymentVO();
  }

  /**
   * 查询订单详情
   * 
   * @param id
   */
  @Override
  public OrderVO orderDetail(Long id) {
    Orders orders = orderMapper.getById(id);
    OrderVO orderVO = new OrderVO();
    BeanUtils.copyProperties(orders, orderVO);
    List<OrderDetail> orderDetails = orderDetailMapper.list(Math.toIntExact(id));
    orderVO.setOrderDetailList(orderDetails);
    AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());
    String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName()
        + addressBook.getDetail();
    orderVO.setAddress(address);
    return orderVO;
  }

  /**
   * 取消订单
   * 
   * @param
   */
  public void cancel(OrdersCancelDTO ordersCancelDTO) {
    Orders orders = orderMapper.getById(ordersCancelDTO.getId());
    orders.setCancelReason(ordersCancelDTO.getCancelReason());
    orders.setStatus(Orders.CANCELLED);
    orders.setCancelTime(LocalDateTime.now());
    orderMapper.cancel(orders);
  }

  /**
   * 历史订单分页查询
   * 
   * @param
   * @return
   * 
   */
  public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
    PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
    ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
    Page<OrderVO> page = orderMapper.historyOrders(ordersPageQueryDTO);
    List<OrderVO> orderVOs = page.getResult();
    for (int i = 0; i < orderVOs.size(); i++) {
      Long orderId = orderVOs.get(i).getId();
      orderVOs.get(i).setOrderDetailList(orderDetailMapper.list(Math.toIntExact(orderId)));
    }
    PageResult pageResult = PageResult.builder()
        .total(page.getTotal())
        .records(orderVOs)
        .build();
    return pageResult;
  }

  /**
   * 再来一单
   * 
   * @param
   */
  @Transactional
  public void repetition(Long id) {
    Orders orders = orderMapper.getById(id);
    orders.setNumber(String.valueOf(System.currentTimeMillis()));
    orders.setOrderTime(LocalDateTime.now());
    orders.setPayStatus(Orders.UN_PAID);
    orders.setStatus(Orders.PENDING_PAYMENT);

    // 插入orders 要主键返回
    orderMapper.insert(orders);
    // 插入orderdetail
    List<OrderDetail> orderDetails = orderDetailMapper.list(Math.toIntExact(id));
    for (int i = 0; i < orderDetails.size(); i++) {
      orderDetails.get(i).setOrderId(orders.getId());
    }
    orderDetailMapper.insert(orderDetails); // 插入orderdetail表

  }
}
