package com.sky.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
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
      for (int j = 0; j < categories.size(); j++) {
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
  public void updateDish(DishDTO dishDTO) { // TODO 有bug 菜品口味无法更新和删除
    log.info("开始修改菜品");
    Dish dish = new Dish();
    BeanUtils.copyProperties(dishDTO, dish);
    dishMapper.updateDish(dish);
    List<DishFlavor> list = dishDTO.getFlavors();
    if (list != null && list.size() > 0) {
      dishFlavorMapper.updateFlavor(list);
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
}
