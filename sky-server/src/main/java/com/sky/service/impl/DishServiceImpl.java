package com.sky.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
  @Autowired
  private DishMapper dishMapper;

  @Autowired
  private DishFlavorMapper dishFlavorMapper;

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private SetmealDishMapper setmealDishMapper;

  @Autowired
  private SetmealMapper setmealMapper;

  /**
   * 新增菜品
   */
  @Transactional
  public void insertDish(DishDTO dishDTO) { // TODO 有空优化插入菜品 名称不能重复
    // 先插入这道菜进菜品表
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.insertDish(dish);
    log.info("菜品插入完成:{}", dish);
    // 抽取出口味表
    List<DishFlavor> flavors = dishDTO.getFlavors();
    // 插入口味表
    if (flavors != null && flavors.size() > 0) {
      dishFlavorMapper.insertFlavor(flavors, dish.getId());
      log.info("口味插入完成:{}", flavors);
    }
  }

  /**
   * 分页查询菜品
   * 
   * @param dishPageQueryDto
   */
  public PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
    PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
    Page<DishVO> page = dishMapper.dishPageQuery(dishPageQueryDTO);
    List<Category> categories = categoryMapper.list(1);
    List<DishVO> dishVOs = page.getResult();
    for (int i = 0; i < dishVOs.size(); i++) {
      for (int j = 0; j < categories.size(); j++) { // 循环查询分类名称name，写入结果
        if (dishVOs.get(i).getCategoryId() == categories.get(j).getId()) {
          dishVOs.get(i).setCategoryName(categories.get(j).getName());
          break;
        }
        continue;
      }
    }
    PageResult pageResult = new PageResult(page.getTotal(), dishVOs);
    return pageResult;
  }

  /**
   * 修改菜品
   * 
   * @param dishDTO
   */
  @Transactional
  public void updateDish(DishDTO dishDTO) {
    log.info("开始修改菜品");
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.updateDish(dish);
    List<DishFlavor> flavors = dishDTO.getFlavors();
    // 直接把口味全删了，改成新传入的
    dishFlavorMapper.deleteFlavorById(dish.getId());
    if (flavors != null && flavors.size() > 0) {
      dishFlavorMapper.insertFlavor(flavors, dish.getId());
    }
    log.info("修改菜品完成");
  }

  /**
   * 根据id查找菜品
   * 
   * @param id
   * @return
   */
  public DishVO getDishById(Long id) {
    Dish dish = dishMapper.getDishById(id);
    DishVO dishVO = new DishVO();
    BeanUtils.copyProperties(dish, dishVO);
    List<DishFlavor> list = dishFlavorMapper.getFlavorsById(id);
    dishVO.setFlavors(list);
    return dishVO;
  }

  /**
   * 根据分类id查找菜品
   * 
   * @param categoryId
   */
  public List<Dish> getDishByCategoryId(Long categoryId) {
    return dishMapper.getDishByCategoryId(categoryId);
  }

  /**
   * 删除菜品
   * 
   * @param
   */
  @Transactional
  public void deleteDish(List<Long> ids) {
    log.info("开始删除");
    List<Dish> dishs = dishMapper.getDishsByIds(ids);
    for (int i = 0; i < dishs.size(); i++) {
      if (dishs.get(i).getStatus() == 1) { // 移除status=1的菜
        log.info("{}为在售，不能删除", dishs.get(i).getName());
        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
      }
      if (setmealDishMapper.countByDishId(dishs.get(i).getId()) > 0) { // 移除关联了套餐的菜品
        log.info("{}关联了套餐，不能删除", dishs.get(i).getName());
        throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
      }
    }

    if (dishs != null && dishs.size() > 0) {
      dishMapper.deleteDish(dishs);
      dishFlavorMapper.deleteFlavor(dishs);
      log.info("删除完成");
    }

  }

  /**
   * 起售或停售
   * 
   * @param
   */
  @Transactional
  public void useOrBan(Integer status, Long id) {
    Dish dish = new Dish();
    dish.setId(id);
    dish.setStatus(status);
    dishMapper.useOrBan(dish);
    if (status == 0) {// 如果停售，包含此菜品的套餐也要停售！
      SetmealDish setmealDish = setmealDishMapper.getSetmealById(id);
      if (setmealDish != null) {
        setmealMapper.banSetmeal(setmealDish.getSetmealId());
      }
    }
  }

  /**
   * 条件查询菜品和口味
   * 
   * @param dish
   * @return
   */
  public List<DishVO> listWithFlavor(Dish dish) {
    List<Dish> dishList = dishMapper.list(dish);

    List<DishVO> dishVOList = new ArrayList<>();

    for (Dish d : dishList) {
      DishVO dishVO = new DishVO();
      BeanUtils.copyProperties(d, dishVO);

      // 根据菜品id查询对应的口味
      List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

      dishVO.setFlavors(flavors);
      dishVOList.add(dishVO);
    }

    return dishVOList;
  }
}
