package com.sky.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private JwtProperties jwtProperties;

  /**
   * 微信登录
   * 
   * @param userLoginDTO
   * @return
   */
  @PostMapping("/login")
  public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
    log.info("用户登录:{}", userLoginDTO.getCode());
    // 微信登录
    User user = userService.userLogin(userLoginDTO);
    // 生成jwt令牌
    Map<String, Object> claims = new HashMap<>();
    claims.put(JwtClaimsConstant.USER_ID, user.getId());
    String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
    // 创建UserLoginVO对象，封装数据
    UserLoginVO userLoginVO = UserLoginVO.builder()
        .id(user.getId())
        .openid(user.getOpenid())
        .token(token)
        .build();
    log.info("登陆成功,token:{},openid:{}", token, user.getOpenid());
    return Result.success(userLoginVO);
  }

  /**
   * 用户登出
   * 
   * @return
   */
  @PostMapping("/logout")
  public Result<Object> logout() {
    log.info("用户登出");
    return Result.success();
  }
}
