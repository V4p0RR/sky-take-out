package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;

@RestController("adminShopController") // 指定bean对象的名称 不然会和用户端冲突
@Slf4j // 店铺控制器
@RequestMapping("/admin/shop")
public class ShopController {
  @Autowired
  private RedisTemplate redisTemplate;

  /**
   * 设置店铺状态
   * 
   * @param status
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PutMapping("/{status}")
  public Result setShopStatus(@PathVariable Integer status) {
    log.info("设置店铺状态:{}", status);
    redisTemplate.opsForValue().set("SHOP_STATUS", status.toString());
    return Result.success();
  }

  /**
   * 获取店铺营业状态
   * 
   * @return
   */
  @GetMapping("/status")
  public Result<Integer> getShopStatus() {
    log.info("获取店铺营业状态");
    Integer status = Integer.valueOf(redisTemplate.opsForValue().get("SHOP_STATUS").toString());
    return Result.success(status);
  }
}
