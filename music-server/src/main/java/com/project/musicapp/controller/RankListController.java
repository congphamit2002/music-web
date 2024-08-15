package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.RankListRequest;
import com.project.musicapp.service.RankListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rankLists")
public class RankListController {
    private final RankListService rankListService;

    @PostMapping("")
    public Response addRankList(@RequestBody RankListRequest rankListRequest) {
        return rankListService.addRank(rankListRequest);
    }

    @GetMapping("/songLists/{songListId}")
    public Response getRankListBySongListId(@PathVariable Long songListId) {
        return rankListService.rankOfSongListId(songListId);
    }

    @GetMapping("/user")
    public Response getRankListByUserId(@RequestParam Long userId, Long songListId) {
        return rankListService.getUserRank(userId, songListId);
    }
}
