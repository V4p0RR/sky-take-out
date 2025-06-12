package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;

import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
  @Autowired
  private ShoppingCartMapper shoppingCartMapper;
  @Autowired
  private DishMapper dishMapper;
  @Autowired
  private SetmealMapper setmealMapper;

  /**
   * 加入购物车
   * 
   * @param
   */
  public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
    // 先封装进购物车实体类
    ShoppingCart shoppingCart = ShoppingCart.builder()
        .userId(BaseContext.getCurrentId())
        .dishId(shoppingCartDTO.getDishId())
        .setmealId(shoppingCartDTO.getSetmealId())
        .dishFlavor(shoppingCartDTO.getDishFlavor())
        .build();
    // 判断购物车里是否有这个套餐或菜品
    if (shoppingCart.getDishId() != null && shoppingCartMapper.getDishByCart(shoppingCart) != null) {
      // 如果有这个菜品，菜品数量加一
      shoppingCart.setNumber(1);
      shoppingCartMapper.updateCart_Dish(shoppingCart);
    } else if (shoppingCart.getSetmealId() != null && shoppingCartMapper.getSetmealByCart(shoppingCart) != null) {
      // 如果有这个套餐，套餐数量加一
      shoppingCart.setNumber(1);
      shoppingCartMapper.updateCart_Setmeal(shoppingCart);
    } else {
      // 如果不存在，则先获取菜品/套餐信息，再插入购物车
      if (shoppingCart.getDishId() != null && shoppingCart.getSetmealId() == null) {
        // 如果是菜品
        Dish dish = dishMapper.getDishById(shoppingCart.getDishId());
        shoppingCart.setName(dish.getName());
        shoppingCart.setImage(dish.getImage());
        shoppingCart.setNumber(1);
        shoppingCart.setAmount(dish.getPrice());
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.addCart_Dish(shoppingCart);
      }
      if (shoppingCart.getDishId() == null && shoppingCart.getSetmealId() != null) {
        // 如果是套餐
        SetmealVO setmealVO = setmealMapper.getSetmealById(shoppingCart.getSetmealId());
        shoppingCart.setName(setmealVO.getName());
        shoppingCart.setImage(setmealVO.getImage());
        shoppingCart.setNumber(1);
        shoppingCart.setAmount(setmealVO.getPrice());
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.addCart_Setmeal(shoppingCart);
      }
    }
  }

  /**
   * 根据userid查询购物车
   * 
   * @param
   * @return
   */
  public List<ShoppingCart> list(Long userId) {
    List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(userId);
    return shoppingCarts;
  }

  /**
   * 删除购物车中一件东西
   * 
   * @param
   */
  public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
    // 先封装进购物车实体类
    ShoppingCart shoppingCart = ShoppingCart.builder()
        .userId(BaseContext.getCurrentId())
        .dishId(shoppingCartDTO.getDishId())
        .setmealId(shoppingCartDTO.getSetmealId())
        .dishFlavor(shoppingCartDTO.getDishFlavor())
        .build();
    // 判断是菜品还是套餐
    if (shoppingCart.getDishId() != null && shoppingCart.getSetmealId() == null) {
      // 判断当前操作应该是删除还是-1
      if (shoppingCartMapper.getDishByCart(shoppingCart).getNumber() != 1) {
        shoppingCartMapper.subShoppingCart(shoppingCart);
      } else {
        shoppingCartMapper.deleteShoppingCart(shoppingCartMapper.getDishByCart(shoppingCart).getId());
      }
    }
    if (shoppingCart.getDishId() == null && shoppingCart.getSetmealId() != null) {
      // 判断当前操作应该是删除还是-1
      if (shoppingCartMapper.getSetmealByCart(shoppingCart).getNumber() != 1) {
        shoppingCartMapper.subShoppingCart(shoppingCart);
      } else {
        shoppingCartMapper.deleteShoppingCart(shoppingCartMapper.getSetmealByCart(shoppingCart).getId());
      }
    }
  }

  /**
   * 清空购物车
   * 
   * @param
   */
  public void clean(Long userId) {
    shoppingCartMapper.clean(userId);
  }
}
