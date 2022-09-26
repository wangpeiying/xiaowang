package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.mapper.OrdersMapper;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jektong
 * @date 2022年05月28日 17:18
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

  public List<OrderDetail> getByOrderId(Long orderId){

      LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(OrderDetail::getOrderId,orderId);
      List<OrderDetail> orderDetails=this.list(queryWrapper);
      return  orderDetails;
  }
}
