package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.Song;
import com.project.musicapp.model.request.SongRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SongService extends IService<Song> {
    Response addSong(SongRequest songRequest, MultipartFile lyricFile, MultipartFile audioFile);

    Response updateSong(int id, SongRequest songRequest);

    Response updateSongUrl(int songId, MultipartFile songFile);

    Response updateSongPicture(int songId, MultipartFile picture);

    Response updateSongLyric(int id, MultipartFile lyricFile);

    Response deleteSong(int id);

    Response allSong();

    Response songOfSingerId(int singerId);

    Response songOfId(Integer id);

    Response songlikeName(String name);

    Song getSongById(int id) throws DataNotFoundException;
}
