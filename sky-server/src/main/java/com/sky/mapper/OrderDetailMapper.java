package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;

@Mapper
public interface OrderDetailMapper {
  /**
   * 插入订单菜品
   * 
   * @param orderDetails
   */
  void insert(List<OrderDetail> orderDetails);

  /**
   * 根据订单id查询菜列表
   * 
   * @param id
   * @return
   */
  List<OrderDetail> list(Integer orderId);

  /**
   * 根据订单列表查询菜品名称
   * 
   * @param ordersList
   * @return
   */
  List<String> getNameByOrders(List<Orders> ordersList);

  /**
   * 根据菜品名称查询菜品销量
   * 
   * @param name
   * @param ordersList
   * @return
   */
  Long getNumberByName(@Param("name") String name, @Param("OrdersList") List<Orders> ordersList);

}
