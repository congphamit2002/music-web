package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.ListSong;
import com.project.musicapp.model.request.ListSongRequest;

public interface ListSongService extends IService<ListSong> {
    Response allListSong();

    Response listSongOfSongListId(int songListId);

    Response addListSong(ListSongRequest listSongRequest);

    Response updateListSong(int id, ListSongRequest listSongRequest);

    Response deleteListSong(int id);

    ListSong getListSongById(int id) throws DataNotFoundException;
}
