package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * 
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品 没有口味
     * 
     * @param dishDTO
     */
    @AutoFill(OperationType.INSERT)
    void insertDish(Dish dish);

    /**
     * 分页查询菜品
     * 
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> dishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查找菜品
     * 
     * @param id
     * @return
     */
    Dish getDishById(Long id);

    /**
     * 修改菜品
     * 
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 根据分类id查找菜品
     * 
     * @param categoryId
     * @return
     */
    List<Dish> getDishByCategoryId(Long categoryId);

}
