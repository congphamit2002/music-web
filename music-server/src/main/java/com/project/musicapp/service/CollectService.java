package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.Collect;
import com.project.musicapp.model.request.CollectRequest;

public interface CollectService extends IService<Collect> {
    Response collectOfUser(int userId);

    Response existSongId(int userId, int songId);

    Response addCollect(CollectRequest collectRequest);

    Response deleteCollect(int userId, int songId);
}
