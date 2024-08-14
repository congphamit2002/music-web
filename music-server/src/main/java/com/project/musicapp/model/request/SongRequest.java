package com.project.musicapp.model.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class SongRequest {
    private Integer id;

    private Integer singerId;

    private String name;

    private String introduction;

    private String pic;

    private String lyric;

    private String url;
}
