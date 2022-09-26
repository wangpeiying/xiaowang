package com.itheima.reggie.controller;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.CommonsConst;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 移动端用户操作
 * @author jektong
 * @date 2022年05月22日 23:37
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 移动端发送短信
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        // 获取手机号
        String phone = user.getPhone();
        // 校验手机号
        Pattern mobile = PatternPool.MOBILE;
        Matcher matcher = mobile.matcher(phone);
        if(!matcher.matches()){
            return R.error("手机号格式有误");
        }

        if(StringUtils.isNotEmpty(phone)){
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4).toString();
            log.info("瑞吉外卖验证码：code为：" + code);
            // 调用阿里云短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            // 将生成的验证码保存
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    @Transactional
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        // 获取手机号
        String phone = (String) map.get("phone");
        // 获取验证码
        String code = (String) map.get("code");
        //从redis中获取保存的验证码
        Object codeSession =redisTemplate.opsForValue().get(phone);
        log.info("redis中获取验证码为："+codeSession);
        // 比对验证码
        if(codeSession!=null&&codeSession.equals(code)){
            // 成功，则登录
            // 判断当前用户是否为新用户，新用户自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            // 手机号查询新用户
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                 user = new User();
                 user.setPhone(phone);
                 user.setStatus(CommonsConst.EMPLOYEE_STATUS_YES);
                 userService.save(user);
            }
            //如果用户登录成功则删除Redis中缓存的验证码
            redisTemplate.delete(phone);
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败,验证码有误");
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理session中的用户id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

    /**
     * 修改用户信息
     * @param user
     * @return R<String>
     */
    @PutMapping
    public R<String> update(@RequestBody User user){
        userService.updateById(user);
        return R.success("菜品修改成功");
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id){
        log.info("根据id查询用户信息...");
        User user = userService.getById(id);
        if (user!=null){
            return R.success(user);
        }
        return R.error("没有查询到相关信息");
    }
}
