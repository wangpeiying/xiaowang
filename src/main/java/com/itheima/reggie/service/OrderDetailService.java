package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;

import java.util.List;

/**
 * @author jektong
 * @date 2022年05月28日 17:17
 */
public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getByOrderId(Long orderId);
}
