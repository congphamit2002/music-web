package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@TableName(value = "user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private Byte sex;

    private String phoneNum;

    private String email;

    private Date birth;

    private String introduction;

    private String avator;

    private Role role;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
