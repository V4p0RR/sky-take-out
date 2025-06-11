package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.OrderDetail;

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

}
