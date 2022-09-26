package com.itheima.reggie.common;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * ControllerAdvice 拦截带有RestController Controller注解的类
 * ResponseBody 返回json数据
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法 @ExceptionHandler处理SQLIntegrityConstraintViolationException异常
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.info(exception.getMessage());
        //错误信息 Duplicate entry 'guwen' for key 'idx_username'
        if (exception.getMessage().contains("Duplicate entry ")){
            String[] split = exception.getMessage().split(" ");//以空格分隔开
            //Duplicate entry 'guwen' for key 'idx_username'对应
            //0          1       2     3   4        5
            String msg="用户"+split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        // 输出异常信息
        log.info(ex.getMessage());

        return R.error(ex.getMessage());
    }

}