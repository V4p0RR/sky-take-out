package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;

@Mapper
public interface DishFlavorMapper {

  /**
   * 插入口味
   * 
   * @param flavors
   */
  void insertFlavor(List<DishFlavor> flavors, Long id);

  /**
   * 修改口味
   * 
   * @param list
   */
  void updateFlavor(DishFlavor dishFlavor);

  /**
   * 根据id查找口味
   * 
   * @param list
   */
  List<DishFlavor> getFlavorsById(Long id);

  /**
   * 根据ids删除口味
   * 
   * @param dishs
   */
  void deleteFlavor(List<Dish> dishs);

  /**
   * 根据dishid删除口味
   * 
   * @param dishs
   */
  void deleteFlavorById(Long dishId);

}
