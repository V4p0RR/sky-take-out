package com.sky.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {
  @Autowired
  private DishService dishService;

  /**
   * 新增菜品
   * 
   * @param dishDTO
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping
  @CacheEvict(cacheNames = { "dishCache", "setmealCache" }, allEntries = true)
  public Result insertDish(@RequestBody DishDTO dishDTO) {
    log.info("新增菜品:{}", dishDTO);
    dishService.insertDish(dishDTO);
    return Result.success();
  }

  /**
   * 分页查询菜品
   * 
   * @param dishPageQueryDTO
   * @return
   */
  @GetMapping("/page")
  public Result<PageResult> dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
    log.info("菜品分类查询：{}", dishPageQueryDTO);
    PageResult pageResult = dishService.dishPageQuery(dishPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 修改菜品
   * 
   * @param dishDTO
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PutMapping
  @CacheEvict(cacheNames = { "dishCache", "setmealCache" }, allEntries = true)
  public Result updateDish(@RequestBody DishDTO dishDTO) {
    log.info("修改菜品:{}", dishDTO);
    dishService.updateDish(dishDTO);
    return Result.success();
  }

  /**
   * 根据id查找菜品
   * 
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public Result<DishVO> getDishById(@PathVariable Long id) {
    log.info("根据id={}查找菜品", id);
    DishVO dishVO = dishService.getDishById(id);
    return Result.success(dishVO);
  }

  /**
   * 根据分类id查找菜品
   * 
   * @param categoryId
   * @return
   */
  @GetMapping("/list")
  public Result<List<Dish>> getDishByCategoryId(@RequestParam Long categoryId) {
    log.info("根据分类id:{}查询菜品", categoryId);
    List<Dish> dishs = dishService.getDishByCategoryId(categoryId);
    return Result.success(dishs);
  }

  /**
   * 删除菜品
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("rawtypes")
  @DeleteMapping("")
  @CacheEvict(cacheNames = { "dishCache", "setmealCache" }, allEntries = true)
  public Result deleteDish(@RequestParam List<Long> ids) {
    log.info("删除菜品：{}", ids.toString());
    dishService.deleteDish(ids);
    return Result.success();
  }

  /**
   * 起售或停售
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping("/status/{status}")
  @CacheEvict(cacheNames = { "dishCache", "setmealCache" }, allEntries = true)
  public Result useOrBan(@PathVariable Integer status, @RequestParam Long id) {
    log.info("设置菜品:{}为{}", id, status);
    dishService.useOrBan(status, id);
    return Result.success();
  }
}
