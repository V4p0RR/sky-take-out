package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

  /**
   * 查找购物车是否有当前菜品
   * 
   * @param shoppingCart
   * @return
   */
  ShoppingCart getDishByCart(ShoppingCart shoppingCart);

  /**
   * 查找购物车是否有当前套餐
   * 
   * @param shoppingCart
   * @return
   */
  ShoppingCart getSetmealByCart(ShoppingCart shoppingCart);

  /**
   * 修改购物车中的菜品
   * 
   * @param shoppingCart
   */
  void updateCart_Dish(ShoppingCart shoppingCart);

  /**
   * 修改购物车中的套餐
   * 
   * @param shoppingCart
   */
  void updateCart_Setmeal(ShoppingCart shoppingCart);

  /**
   * 添加购物车-菜品
   * 
   * @param shoppingCart
   */
  void addCart_Dish(ShoppingCart shoppingCart);

  /**
   * 添加购物车-套餐
   * 
   * @param shoppingCart
   */
  void addCart_Setmeal(ShoppingCart shoppingCart);

  /**
   * 根据userid查询购物车
   * 
   * @param userId
   * @return
   */
  List<ShoppingCart> list(Long userId);

  /**
   * 删除购物车中一件东西(-1)
   * 
   * @param shoppingCart
   */
  void subShoppingCart(ShoppingCart shoppingCart);

  /**
   * 直接删除购物车一个东西
   * 
   * @param shoppingCart
   */
  void deleteShoppingCart(Long id);

  /**
   * 清空购物车
   * 
   * @param userId
   */
  void clean(Long userId);

}
