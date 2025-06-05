package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sky.entity.SetmealDish;

@Mapper
/**
 * 这是套餐与菜品关系的mapper
 */
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

  /**
   * 插入套餐菜品关系
   * 
   * @param setmealDishes
   */
  void insertSetmealDish(List<SetmealDish> setmealDishes, Long setmealId);

  /**
   * 根据套餐id查找所有菜品
   * 
   * @param categoryId
   * @return
   */
  List<SetmealDish> getDishesBySetmealId(Long setmealId);

  /**
   * 根据套餐id删除菜品关系
   * 
   * @param id
   */
  void deleteSetmealDishById(Long setmealId);

  /**
   * 根据ids删除套餐菜品关系
   * 
   * @param ids
   */
  void deleteSetmealsByIds(Long[] ids);
}
