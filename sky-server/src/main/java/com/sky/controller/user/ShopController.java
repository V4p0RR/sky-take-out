package com.sky.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;

@RestController("userShopController") // 指定bean的名称 避免冲突
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
  @SuppressWarnings("rawtypes")
  @Autowired
  private RedisTemplate redisTemplate;

  /**
   * 获取店铺营业状态
   * 
   * @return
   */
  @GetMapping("/status")
  @Cacheable(cacheNames = "shopCache", key = "'status'")
  public Result<Integer> getShopStatus() {
    log.info("获取店铺营业状态");
    Integer status = Integer.valueOf(redisTemplate.opsForValue().get("SHOP_STATUS").toString());
    return Result.success(status);
  }

}
