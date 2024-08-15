package com.project.musicapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.musicapp.model.domain.RankList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RankListMapper extends BaseMapper<RankList> {
    Integer selectScoreSum(Long songListId);
    Integer selectUserRank(@Param("user_id") Long userId, @Param("song_list_id")  Long songListId);
}
