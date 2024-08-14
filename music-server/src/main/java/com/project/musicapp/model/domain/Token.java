package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "token")
@Data
public class Token {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String accessToken;

    private Boolean isLoggedOut;

    private Integer userId;
};
