package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "user_support")
@Data
public class UserSupport {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer commentId;

    private Integer userId;
}
