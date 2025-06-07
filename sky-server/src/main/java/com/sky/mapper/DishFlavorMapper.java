package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

  /**
   * 根据菜品id查询对应的口味数据
   * 
   * @param dishId
   * @return
   */
  @Select("select * from dish_flavor where dish_id = #{dishId}")
  List<DishFlavor> getByDishId(Long dishId);
}
