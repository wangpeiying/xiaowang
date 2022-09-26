package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据：{}", shoppingCart);

        //设置用户id，指定当前是哪个用户的的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        }
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            // 如果不存在，添加到购物车，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 套餐或者是菜品数量减少设置
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    @Transactional
    //携带的参数可能是dish_id也可能是setmeal_id所以我们需要用shoppingCart接收
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        //菜品id
        Long dishId = shoppingCart.getDishId();
        //套餐id
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        //代表数量减少的是菜品数量
        if (dishId != null) {
            //通过dishId查出购物车菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            //查询当前用户对应的购物车
            queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            ShoppingCart cartDish = shoppingCartService.getOne(queryWrapper);
            cartDish.setNumber(cartDish.getNumber() - 1);
            Integer number = cartDish.getNumber();
            if (number > 0) {
                //对数据进行更新
                shoppingCartService.updateById(cartDish);
            } else if (number == 0) {
                //如果购物车的数量为0，那么就将菜品从购物车删除
                shoppingCartService.removeById(cartDish.getId());
            } else if (number < 0) {
                return R.error("操作异常");
            }
            return R.success(cartDish);
        }
        //代表数量减少的是套餐数量
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
            //查询当前用户对应的购物车
            queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            ShoppingCart cartSetmeal = shoppingCartService.getOne(queryWrapper);
            cartSetmeal.setNumber(cartSetmeal.getNumber() - 1);
            Integer number = cartSetmeal.getNumber();
            if (number > 0) {
                //对数据进行更新
                shoppingCartService.updateById(cartSetmeal);
            } else if (number == 0) {
                //如果购物车的数量为0，那么就将套餐从购物车删除
                shoppingCartService.removeById(cartSetmeal);
            } else if (number < 0) {
                return R.error("操作异常");
            }
            return R.success(cartSetmeal);
        }
        //如果菜品和套餐都进不去
        return R.error("操作异常");
    }


    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        //SQL：Delete from shopping_cart where user_id = ?
        shoppingCartService.clean();
        return R.success("清空购物车成功");
    }
}

