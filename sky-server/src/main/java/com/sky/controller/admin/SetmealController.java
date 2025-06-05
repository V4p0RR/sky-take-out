package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
// 套餐管理控制器
public class SetmealController {
  @Autowired
  private SetmealService setmealService;

  /**
   * 插入套餐
   * 
   * @param setmealDTO
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping
  public Result insertSetmeal(@RequestBody SetmealDTO setmealDTO) {
    log.info("插入套餐:{}", setmealDTO);
    setmealService.insertSetmeal(setmealDTO);
    return Result.success();
  }

  /**
   * 分页查询套餐
   * 
   * @param setmealPageQueryDTO
   * @return
   */
  @GetMapping("/page")
  public Result<PageResult> pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
    log.info("分页查询套餐:{}", setmealPageQueryDTO);
    PageResult pageResult = setmealService.pageQuerySetmeal(setmealPageQueryDTO);
    return Result.success(pageResult);
  }

  /**
   * 根据id查询套餐
   * 
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
    log.info("根据id:{}查询套餐", id);
    SetmealVO setmealVO = setmealService.getSetmealById(id);
    return Result.success(setmealVO);
  }

  /**
   * 修改套餐
   * 
   * @param setmealDTO
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PutMapping
  public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
    log.info("修改套餐{}", setmealDTO);
    setmealService.updateSetmeal(setmealDTO);
    return Result.success();
  }

  /**
   * 删除套餐
   * 
   * @return
   */
  @DeleteMapping
  @SuppressWarnings("rawtypes")
  public Result deleteSetmeal(Long[] ids) {
    log.info("删除套餐：{}", ids.toString());
    setmealService.deleteSetmeal(ids);
    return Result.success();
  }

  /**
   * 启用/禁用套餐
   * 
   * @param
   * @return
   */
  @SuppressWarnings("rawtypes")
  @PostMapping("/status/{status}")
  public Result useOrBanSetmeal(@PathVariable Integer status, Long id) {
    log.info("设置id为{}套餐状态为{}", id, status);
    setmealService.useOrBanSetmeal(status, id);
    return Result.success();
  }
}
