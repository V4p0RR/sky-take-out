package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {

  /**
   * 插入口味
   * 
   * @param flavors
   */
  void insertFlavor(List<DishFlavor> flavors, Long id);
}
