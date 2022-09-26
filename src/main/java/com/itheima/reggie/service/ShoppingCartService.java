package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;

/**
 * @author jektong
 * @date 2022年05月28日 0:21
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}
