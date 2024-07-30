package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.ListSongMapper;
import com.project.musicapp.model.domain.ListSong;
import com.project.musicapp.model.request.ListSongRequest;
import com.project.musicapp.service.ListSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListSongServiceImpl extends ServiceImpl<ListSongMapper, ListSong> implements ListSongService {
    private final ListSongMapper listSongMapper;

    @Override
    public Response allListSong() {
        try {
            return Response.success(null, listSongMapper.selectList(null));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response listSongOfSongListId(int songListId) {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_list_id", songListId);
        return Response.success(null, listSongMapper.selectList(queryWrapper));
    }

    @Override
    public Response addListSong(ListSongRequest listSongRequest) {
        try {
            ListSong listSong = new ListSong();
            BeanUtils.copyProperties(listSongRequest, listSong);
            if (listSongMapper.insert(listSong) > 0) {
                return Response.success("Insert list song success");
            } else {
                return Response.error("Insert list song failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateListSong(int id, ListSongRequest listSongRequest) {
        try {
            ListSong listSong = this.getListSongById(id);
            BeanUtils.copyProperties(listSongRequest, listSong, "id");
            if (listSongMapper.updateById(listSong) > 0) {
                return Response.success("Update list song success");
            } else {
                return Response.error("Update list song failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteListSong(int id) {
        try {
            ListSong listSong = this.getListSongById(id);
            if (listSongMapper.deleteById(listSong) > 0) {
                return Response.success("Delete list song success");
            } else {
                return Response.error("Delete list song failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public ListSong getListSongById(int id) throws DataNotFoundException {
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        ListSong listSong = listSongMapper.selectOne(queryWrapper);
        if (listSong == null) {
            throw new DataNotFoundException("Can not find the list song by id " + id);
        }
        return listSong;
    }
}
