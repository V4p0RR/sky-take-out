package com.sky.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
  @Autowired
  private ShoppingCartService shoppingCartService;

  /**
   * 加入购物车
   * 
   * @param shoppingCartDTO
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping("/add")
  public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    log.info("加入购物车:{}", shoppingCartDTO);
    shoppingCartService.addShoppingCart(shoppingCartDTO);
    return Result.success();
  }

  /**
   * 根据userid查询购物车
   * 
   * @return
   */
  @GetMapping("/list")
  public Result<List<ShoppingCart>> list() {
    Long userId = BaseContext.getCurrentId();
    log.info("查询用户id:{}的购物车", userId);
    List<ShoppingCart> shoppingCarts = shoppingCartService.list(userId);
    return Result.success(shoppingCarts);
  }

  /**
   * 删除购物车的一个东西
   * 
   * @param shoppingCartDTO
   * @return
   */
  @PostMapping("/sub")
  @SuppressWarnings("rawtypes")
  public Result subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
    log.info("删除购物车中一件:{}", shoppingCartDTO);
    shoppingCartService.subShoppingCart(shoppingCartDTO);
    return Result.success();
  }

  /**
   * 清空购物车
   * 
   * @return
   */
  @DeleteMapping("/clean")
  @SuppressWarnings("rawtypes")
  public Result clean() {
    shoppingCartService.clean(BaseContext.getCurrentId());
    return Result.success();
  }
}
