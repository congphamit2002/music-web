package com.project.musicapp.model.request;

import lombok.Data;

@Data
public class RankListRequest {
    private Integer id;

    private Integer songListId;

    private Integer userId;

    private Integer score;
}
