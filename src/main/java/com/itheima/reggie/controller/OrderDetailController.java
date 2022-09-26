package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jektong
 * @date 2022年05月28日 17:20
 */
@RestController
@RequestMapping("/orderdetail")
@Slf4j
public class OrderDetailController {

    @Resource
    private OrdersService ordersService;



}
