package com.project.musicapp.model.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
    private String username;

    private String oldPassword;

    private String password;

    private Byte sex;

    private String phoneNum;

    private String email;

    private Date birth;

    private String introduction;

    private String avator;

    private Date createTime;

    private Date updateTime;

    private String role;
}
