package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.SongListRequest;
import com.project.musicapp.service.SongListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("songLists")
public class SongListController {
    private final SongListService songListService;

    @GetMapping("")
    public Response getAllSongList() {
        return songListService.allSongList();
    }

    @GetMapping("/title")
    public Response songListLikeTitle(@RequestParam String title) {
        return songListService.likeTitle(title);
    }

    @GetMapping("/style")
    public Response songListLikeStyle(@RequestParam String style) {
        return songListService.likeStyle(style);
    }

    @PostMapping("")
    public Response addSongList(@RequestBody SongListRequest songListRequest) {
        return songListService.addSongList(songListRequest);
    }

    @PutMapping("/{id}")
    public Response updateSongList(@PathVariable Integer id, @RequestBody SongListRequest songListRequest) {
        return songListService.updateSongList(id, songListRequest);
    }

    @DeleteMapping("/{id}")
    public Response deleteSongList(@PathVariable Integer id) {
        return songListService.deleteSongList(id);
    }

    @PutMapping("/{id}/avatar")
    public Response updateSongListAvatar(@PathVariable Integer id, @RequestParam MultipartFile file) {
        return songListService.updateSongListImg(id, file);
    }
}
