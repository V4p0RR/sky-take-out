package com.sky.service;

import java.util.List;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

public interface ShoppingCartService {

  /**
   * 加入购物车
   * 
   * @param shoppingCartDTO
   */
  void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

  /**
   * 根据userid查询购物车
   * 
   * @param userId
   * @return
   */
  List<ShoppingCart> list(Long userId);

  /**
   * 删除购物车中一件东西
   * 
   * @param shoppingCartDTO
   */
  void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

  /**
   * 清空购物车
   * 
   * @param currentId
   */
  void clean(Long userId);

}
