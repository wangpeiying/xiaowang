package com.itheima.reggie.common;



//公共字段填充修改器
public class BaseContext{
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<Long>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
