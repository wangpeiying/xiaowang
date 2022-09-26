package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据
     void saveWithFlover(DishDto dishDto);
    /**
     * 根据ID查询菜品信息以及对应的口味信息
     * @return
     */
     DishDto getByIdWithFlavor(Long id);
    /**
     * 更新菜品信息同时更新口味信息
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 根据ID删除菜品
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
