package com.sky.service;

import java.util.List;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

public interface SetmealService {

  /**
   * 插入套餐
   * 
   * @param setmealDTO
   */
  void insertSetmeal(SetmealDTO setmealDTO);

  /**
   * 分页查询套餐
   * 
   * @param setmealPageQueryDTO
   * @return
   */
  PageResult pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

  /**
   * 根据id查询套餐
   * 
   * @param id
   * @return
   */
  SetmealVO getSetmealById(Long id);

  /**
   * 修改套餐
   * 
   * @param setmealDTO
   */
  void updateSetmeal(SetmealDTO setmealDTO);

  /**
   * 删除套餐
   * 
   * @param ids
   */
  void deleteSetmeal(Long[] ids);

  /**
   * 启用/禁用套餐
   * 
   * @param status
   * @param id
   */
  void useOrBanSetmeal(Integer status, Long id);

  /**
   * 条件查询
   * 
   * @param setmeal
   * @return
   */
  List<Setmeal> list(Setmeal setmeal);

  /**
   * 根据id查询菜品选项
   * 
   * @param id
   * @return
   */
  List<DishItemVO> getDishItemById(Long id);
}
