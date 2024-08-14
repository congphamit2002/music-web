package com.project.musicapp.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "rank_list")
@Data
public class RankList {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer songListId;

    private Integer userId;

    private Integer score;
}
