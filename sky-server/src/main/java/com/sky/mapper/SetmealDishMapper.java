package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sky.entity.SetmealDish;

@Mapper
public interface SetmealDishMapper {

  /**
   * 根据菜品id查找套餐数量
   * 
   * @return
   */
  @Select("select count(*) from setmeal_dish where dish_id=#{id}")
  Long countByDishId(Long id);

  /**
   * 根据菜品id查找套餐
   * 
   * @param id
   */
  @Select("select * from setmeal_dish where dish_id=#{id}")
  SetmealDish getSetmealById(Long id);
}
