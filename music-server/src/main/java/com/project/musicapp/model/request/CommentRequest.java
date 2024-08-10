package com.project.musicapp.model.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class CommentRequest {
    private Integer id;

    private Integer userId;

    private Integer songId;

    private Integer songListId;

    private String content;

    private Byte type;

    private Byte up;
}
