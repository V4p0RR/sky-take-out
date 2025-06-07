package com.sky.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

@Mapper
/**
 * 这是套餐的mapper
 */
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * 
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id禁用套餐
     * 
     * @param id
     */
    @Update("update setmeal set status = 0 where id=#{id}")
    void banSetmeal(Long id);

    /**
     * 插入套餐
     * 
     * @param setmealDTO
     */
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(Setmeal setmeal);

    /**
     * 分页查询套餐
     * 
     * @param name
     * @param categoryId
     * @param status
     * @return
     */
    Page<SetmealVO> pageQuerySetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐id查询套餐
     * 
     * @param id
     * @return
     */
    SetmealVO getSetmealById(Long id);

    /**
     * 修改套餐
     * 
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);

    /**
     * 根据ids删除套餐
     * 
     * @param ids
     */
    void deleteSetmeal(Long[] ids);

    /**
     * 根据ids查询套餐list
     * 
     * @param ids
     * @return
     */
    List<Setmeal> getSetmealsByIds(Long[] ids);

    /**
     * 启用/禁用套餐
     * 
     * @param status
     * @param id
     */
    @AutoFill(OperationType.UPDATE)
    void useOrBan(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     * 
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * 
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

}
