package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * @author jektong
 * @date 2022/4/21
 */
@Data
public class Employee implements Serializable {

    /** 序列号*/
    private static final long serialVersionUID = 1L;

    /**唯一主键*/
    private Long id;

    /**用户名*/
    private String username;

    /**姓名*/
    private String name;

    /**密码*/
    private String password;

    /**电话*/
    private String phone;

    /**性别*/
    private String sex;

    /**身份证号码*/
    private String idNumber;

    /**状态*/
    private Integer status;

    /**创建时间*/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**更新时间*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**添加用户时使用*/
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**更新用户时使用*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}