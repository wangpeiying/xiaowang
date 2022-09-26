package com.itheima.reggie.filter;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //获取本次处理请求的URL
        String requestURI = request.getRequestURI();
        log.info("拦截到请求{}",requestURI);
        //定义不需要处理的请求路径
        String urls[]=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",    //移动端发送短信
                "/user/login"       //移动端登录
        };
        //判断本次请求是否需要处理 方法处理
        boolean check = check(urls, requestURI);
        //若不需要，直接放行
        if (check){
            log.info("本次请求{}不需要处理，直接放行...",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登陆状态，若已经登陆直接放行 获取session
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，id为..."+request.getSession().getAttribute("employee"));
            Long empId=(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //4-2 判断登陆状态，若已经登陆直接放行(移动端)
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，id为：{}", request.getSession().getAttribute("user"));

            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);
            long id = Thread.currentThread().getId();
            log.info("线程ID为：{}", id);
            filterChain.doFilter(request, response);
            return;
        }
        //若未登陆则返回未登陆结果,通过输出流方式向客户端页面响应数据
        //NOTLOGIN来自于backend/js/request.js 响应拦截器
        log.info("用户未登录...");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    /**
     *
     * @param urls 不需要处理的路径
     * @param requestURL 本次请求的路径
     * @return
     */
    public boolean check(String urls[],String requestURL){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match){
                return true;
            }
        }
        //如果遍历完 没有返回true 证明没有匹配上 返回false
        return false;
    }
}