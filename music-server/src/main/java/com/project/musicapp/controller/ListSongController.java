package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.ListSong;
import com.project.musicapp.model.request.ListSongRequest;
import com.project.musicapp.service.ListSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/listSongs")
public class ListSongController {
    private final ListSongService listSongService;

    @GetMapping("")
    public Response allListSong() {
        return listSongService.allListSong();
    }

    @GetMapping("/songList/{songListId}")
    public Response listSongOfSongListId(@PathVariable int songListId) {
        return listSongService.listSongOfSongListId(songListId);
    }

    @PostMapping("")
    public Response addListSong(@RequestBody ListSongRequest listSongRequest) {
        return listSongService.addListSong(listSongRequest);
    }

    @PutMapping("/{id}")
    public Response updateListSong(@PathVariable int id, @RequestBody ListSongRequest listSongRequest) {
        return listSongService.updateListSong(id, listSongRequest);
    }

    @DeleteMapping("/{id}")
    public Response deleteListSong(@PathVariable int id) {
        return listSongService.deleteListSong(id);
    }
}
