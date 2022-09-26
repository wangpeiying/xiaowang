package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;

import java.util.List;

/**
 * @author jektong
 * @date 2022年05月28日 17:17
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交用户订单
     * @param orders
     */
    void submit(Orders orders);


}
