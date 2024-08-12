package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.mapper.RankListMapper;
import com.project.musicapp.model.domain.Comment;
import com.project.musicapp.model.domain.RankList;
import com.project.musicapp.model.request.RankListRequest;
import com.project.musicapp.service.RankListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankListServiceImpl extends ServiceImpl<RankListMapper, RankList> implements RankListService {
    private final RankListMapper rankListMapper;

    @Override
    public Response addRank(RankListRequest rankListRequest) {
        RankList rankList = new RankList();
        BeanUtils.copyProperties(rankListRequest, rankList);
        try {
            if (rankListMapper.insert(rankList) > 0) {
                return Response.success("Insert rank list success");
            } else {
                return Response.error("Insert rank list failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response rankOfSongListId(Long songListId) {
        try {
            QueryWrapper<RankList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("song_list_id",songListId);
            Long rankNum = rankListMapper.selectCount(queryWrapper);
            return Response.success(null, (rankNum <= 0) ? 0 : rankListMapper.selectScoreSum(songListId) / rankNum);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response getUserRank(Long userId, Long songListId) {
        try {
            return Response.success(null, rankListMapper.selectUserRank(userId, songListId));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }
}
