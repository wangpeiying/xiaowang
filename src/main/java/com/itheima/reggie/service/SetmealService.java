package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐以及新增套餐关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);


    /**
     * 根据ID删除套餐信息，同时删除所关联的菜品
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 修改套餐
     * @param setmealDto
     */
    void updateWithSetmeal(SetmealDto setmealDto);

    /**
     * 根据ID查套餐信息
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);
}