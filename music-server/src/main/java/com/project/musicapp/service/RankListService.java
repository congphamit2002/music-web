package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.RankList;
import com.project.musicapp.model.request.RankListRequest;

public interface RankListService extends IService<RankList> {
    Response addRank(RankListRequest rankListRequest);

    Response rankOfSongListId(Long songListId);

    Response getUserRank(Long userId, Long songListId);
}
