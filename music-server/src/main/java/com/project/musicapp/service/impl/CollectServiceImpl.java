package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.mapper.CollectMapper;
import com.project.musicapp.model.domain.Collect;
import com.project.musicapp.model.request.CollectRequest;
import com.project.musicapp.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {
    private final CollectMapper collectMapper;

    @Override
    public Response collectOfUser(int userId) {
        try {
            QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            return Response.success(null, collectMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response existSongId(int userId, int songId) {
        try {
            QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("song_id", songId);
            if (collectMapper.selectCount(queryWrapper) > 0) {
                return Response.success(null, true);
            } else {
                return Response.success(null, false);
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response addCollect(CollectRequest collectRequest) {
        try {
            Collect collect = new Collect();
            BeanUtils.copyProperties(collectRequest, collect);
            if (collectMapper.insert(collect) > 0) {
                return Response.success("Insert collection success");
            } else {
                return Response.error("Insert collection failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteCollect(int userId, int songId) {
        try {
            QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("song_id", songId);
            if (collectMapper.selectCount(queryWrapper) == 0) {
                return Response.notFound("Can not find collection of userId " + userId + " songId " + songId);
            }
            if (collectMapper.delete(queryWrapper) > 0) {
                return Response.success("Delete collection success");
            } else {
                return Response.error("Delete collection failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }
}
