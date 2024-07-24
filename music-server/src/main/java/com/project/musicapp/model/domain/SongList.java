package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "song_list")
@Data
public class SongList {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String pic;

    private String introduction;

    private String style;
}
