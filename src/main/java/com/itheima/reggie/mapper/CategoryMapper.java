package com.itheima.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper     //声明mapper代理开发
public interface CategoryMapper extends BaseMapper<Category> {
}
