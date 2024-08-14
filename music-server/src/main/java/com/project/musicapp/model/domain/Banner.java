package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "banner")
@Data
public class Banner {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String pic;
}
