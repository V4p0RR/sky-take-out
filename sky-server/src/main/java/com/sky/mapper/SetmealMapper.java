package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
