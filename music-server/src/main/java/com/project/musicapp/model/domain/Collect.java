package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

@TableName(value = "collect")
public class Collect {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer type;

    private Integer songId;

    private Integer songListId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
