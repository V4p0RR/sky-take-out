package com.sky.service;

import java.util.List;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

public interface DishService {

  /**
   * 新增菜品
   * 
   * @param dishDTO
   */
  void insertDish(DishDTO dishDTO);

  /**
   * 分页查询菜品
   * 
   * @param dishPageQueryDTO
   * @return
   */
  PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

  /**
   * 修改菜品
   * 
   * @param dishDTO
   */
  void updateDish(DishDTO dishDTO);

  /**
   * 根据id查找菜品
   * 
   * @param id
   * @return
   */
  DishVO getDishById(Long id);

  /**
   * 根据分类id查询菜品
   * 
   * @param categoryId
   * @return
   */
  List<Dish> getDishByCategoryId(Long categoryId);

  /**
   * 删除菜品
   * 
   * @param ids
   */
  void deleteDish(List<Long> ids);

  /**
   * 起售或停售
   * 
   * @param status
   * @param id
   */
  void useOrBan(Integer status, Long id);

}
