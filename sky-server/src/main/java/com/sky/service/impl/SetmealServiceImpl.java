package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
  @Autowired
  private SetmealMapper setmealMapper;
  @Autowired
  private SetmealDishMapper setmealDishMapper;
  @Autowired
  private CategoryMapper categoryMapper;
  @Autowired
  private DishMapper dishMapper;

  /**
   * 插入套餐
   * 
   * @param setmealDTO
   */
  @Transactional
  public void insertSetmeal(SetmealDTO setmealDTO) {
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal); // 先往setmeal表里插入套餐数据
    setmealMapper.insertSetmeal(setmeal);
    // 再往套餐菜品表里插入数据
    List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
    setmealDishMapper.insertSetmealDish(setmealDishes, setmeal.getId());
    log.info("插入完成");
  }

  /**
   * 分页查询套餐
   * 
   * @param
   */
  public PageResult pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
    PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
    Page<SetmealVO> page = setmealMapper.pageQuerySetmeal(setmealPageQueryDTO);
    List<SetmealVO> setmealVOs = page.getResult();
    for (int i = 0; i < setmealVOs.size(); i++) {
      String categoryName = categoryMapper.getNameById(setmealVOs.get(i).getCategoryId());
      setmealVOs.get(i).setCategoryName(categoryName);
    }
    PageResult pageResult = new PageResult(page.getTotal(), setmealVOs);
    return pageResult;
  }

  /**
   * 根据id查询套餐
   * 
   * @return
   */
  public SetmealVO getSetmealById(Long id) {
    SetmealVO setmealVO = new SetmealVO();
    SetmealVO setmealVO2 = setmealMapper.getSetmealById(id);
    BeanUtils.copyProperties(setmealVO2, setmealVO);
    setmealVO.setCategoryName(categoryMapper.getNameById(setmealVO.getCategoryId()));
    List<SetmealDish> setmealDishs = setmealDishMapper.getDishesBySetmealId(id);
    setmealVO.setSetmealDishes(setmealDishs);
    return setmealVO;
  }

  /**
   * 修改套餐
   * 
   * @param
   */
  @Transactional
  public void updateSetmeal(SetmealDTO setmealDTO) {
    List<SetmealDish> setmealDishs = setmealDTO.getSetmealDishes();
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal);
    setmealMapper.updateSetmeal(setmeal);
    // 先删除原有的菜品关系，再插入现在的菜品关系
    setmealDishMapper.deleteSetmealDishById(setmealDTO.getId());
    setmealDishMapper.insertSetmealDish(setmealDishs, setmealDTO.getId());
  }

  /**
   * 删除套餐
   * 
   * @param
   */
  @Transactional
  public void deleteSetmeal(Long[] ids) {
    if (ids.length == 0) {
      throw new DeletionNotAllowedException("未选择套餐！");
    }
    // 先判断套餐list是否包含起售的套餐
    List<Setmeal> setmeals = setmealMapper.getSetmealsByIds(ids);
    for (int i = 0; i < setmeals.size(); i++) {
      if (setmeals.get(i).getStatus() == 1) {
        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
      }
    }
    setmealMapper.deleteSetmeal(ids);// 先删除套餐表setmeal里的数据
    setmealDishMapper.deleteSetmealsByIds(ids);
    log.info("删除完成");
  }

  /**
   * 启用/禁用套餐
   * 
   * @param
   */
  public void useOrBanSetmeal(Integer status, Long id) {
    // 先判断套餐内有没有禁用的菜品,并且要启用套餐
    if (status == 1) {
      List<SetmealDish> setmealDishs = setmealDishMapper.getDishesBySetmealId(id);
      for (int i = 0; i < setmealDishs.size(); i++) {
        Long dishId = setmealDishs.get(i).getDishId();
        if (dishMapper.getDishById(dishId).getStatus() == 0) {
          throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        }
      }
    }
    // 再设置套餐状态
    Setmeal setmeal = Setmeal.builder()
        .status(status)
        .id(id)
        .build();
    setmealMapper.useOrBan(setmeal);
    log.info("设置套餐状态完成");
  }
}
