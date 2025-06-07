package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.User;

@Mapper
public interface UserMapper {

  /**
   * 根据openid查找user
   * 
   * @param openid
   * @return
   */
  User getUserByOpenid(String openid);

  /**
   * 新增user
   * 
   * @param user
   */
  void insertUser(User user);

}
