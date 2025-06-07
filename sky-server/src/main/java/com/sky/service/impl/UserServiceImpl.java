package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  // 请求微信登录地址前缀
  private final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
  @Autowired // 注入微信相关的属性
  private WeChatProperties weChatProperties;
  @Autowired
  private UserMapper userMapper;

  /**
   * 微信登录
   * 
   * @param
   * @return
   */
  public User userLogin(UserLoginDTO userLoginDTO) {
    String openid = getOpenid(userLoginDTO.getCode());
    // 判断openid是否为空
    if (openid == null) {
      log.info("openid获取空");
      throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
    }
    // 判断是否为新用户
    User user = userMapper.getUserByOpenid(openid);
    if (user == null) {
      // 如果为新用户，自动注册
      log.info("当前为新用户，自动注册");
      user = User.builder()
          .openid(openid)
          .createTime(LocalDateTime.now())
          .build();
      userMapper.insertUser(user);
    }

    return user;
  }

  /**
   * 根据code获取用户openid
   * 
   * @param
   * @return
   */
  private String getOpenid(String code) {
    // 封装请求体map
    Map<String, String> map = new HashMap<>();
    map.put("appid", weChatProperties.getAppid());
    map.put("secret", weChatProperties.getSecret());
    map.put("js_code", code);
    map.put("grant_type", "authorization_code");
    // 调用微信接口，获取openid
    String json = HttpClientUtil.doGet(WX_LOGIN, map);
    // 解析返回体，获取openid
    JSONObject jsonObject = JSON.parseObject(json);
    String openid = jsonObject.getString("openid");
    return openid;
  }

}
