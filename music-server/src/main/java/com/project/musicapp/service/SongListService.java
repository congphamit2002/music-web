package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.SongList;
import com.project.musicapp.model.request.SongListRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SongListService extends IService<SongList> {
    Response addSongList(SongListRequest songListRequest);

    Response updateSongList(int id, SongListRequest songListRequest);

    Response updateSongListImg(int id, MultipartFile image);

    Response deleteSongList(int id);

    Response allSongList();

    Response likeTitle(String title);

    Response likeStyle(String style);

    SongList getSongListById(int id) throws DataNotFoundException;
}
