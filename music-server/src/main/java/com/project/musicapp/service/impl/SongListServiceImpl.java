package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.constant.Constants;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.SongListMapper;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.domain.SongList;
import com.project.musicapp.model.request.SongListRequest;
import com.project.musicapp.service.MinioService;
import com.project.musicapp.service.SongListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SongListServiceImpl extends ServiceImpl<SongListMapper, SongList> implements SongListService {
    private final SongListMapper songListMapper;
    private final MinioService minioService;

    @Override
    public Response addSongList(SongListRequest songListRequest) {
        SongList songList = new SongList();
        BeanUtils.copyProperties(songListRequest, songList);
        songList.setPic("/songList/songList.jpg");
        try {
            if (songListMapper.insert(songList) > 0) {
                return Response.success("Insert song list success");
            } else {
                return Response.error("Insert song list failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSongList(int id, SongListRequest songListRequest) {
        try {
            SongList songList = this.getSongListById(id);
            BeanUtils.copyProperties(songListRequest, songList, "id");
            if(songListMapper.updateById(songList) > 0) {
                return Response.success("Update song list success");
            } else {
                return Response.error("Update song list failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateSongListImg(int id, MultipartFile image) {
        try {
            SongList songList = this.getSongListById(id);
            minioService.uploadImage(image);
            songList.setPic(Constants.UPLOAD_SONG_LIST + image.getOriginalFilename());
            if(songListMapper.updateById(songList) > 0) {
                return Response.success("Update song list avatar success");
            } else {
                return Response.error("Update song list avatar failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteSongList(int id) {
        try {
            SongList songList = this.getSongListById(id);
            if(songListMapper.deleteById(songList) > 0) {
                return Response.success("Delete song list success");
            } else {
                return Response.error("Delete song list failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response allSongList() {
        try {
            return Response.success(null, songListMapper.selectList(null));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response likeTitle(String title) {
        try {
            QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("title", title);
            return Response.success(null, songListMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response likeStyle(String style) {
        try {
            QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("style", style);
            return Response.success(null, songListMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public SongList getSongListById(int id) throws DataNotFoundException {
        QueryWrapper<SongList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SongList songList = songListMapper.selectOne(queryWrapper);
        if (songList == null) {
            throw new DataNotFoundException("Can not find singer by id " + id);
        }
        return songList;
    }
}
