package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jektong
 * @date 2022年05月22日 23:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
