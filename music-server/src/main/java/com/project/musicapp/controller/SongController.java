package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.SongRequest;
import com.project.musicapp.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @GetMapping("")
    public Response getAllSongs() {
        return songService.allSong();
    }

    @GetMapping("/{id}")
    public Response getSongById(@PathVariable int id) {
        return songService.songOfId(id);
    }

    @PostMapping("")
    public Response addSong(@RequestBody SongRequest songRequest, @RequestParam MultipartFile lyricFile, @RequestParam MultipartFile audioFile) {
        return songService.addSong(songRequest, lyricFile, audioFile);
    }

    @PutMapping("/{id}")
    public Response updateSong(@PathVariable int id,@RequestBody SongRequest songRequest) {
        return songService.updateSong(id, songRequest);
    }

    @PutMapping("/{id}/songUrl")
    public Response updateSongUrl(@PathVariable int id, @RequestParam MultipartFile songFile) {
        return songService.updateSongUrl(id, songFile);
    }

    @PutMapping("/{id}/picture")
    public Response updateSongPicture(@PathVariable int id, @RequestParam MultipartFile songPicture) {
        return songService.updateSongPicture(id, songPicture);
    }

    @PutMapping("/{id}/lyric")
    public Response updateSongLyric(@PathVariable int id, @RequestParam MultipartFile lyricFile) {
        return songService.updateSongLyric(id, lyricFile);
    }

    @DeleteMapping("/{id}")
    public Response deleteSong(@PathVariable int id) {
        return songService.deleteSong(id);
    }

    @GetMapping("/singers/{singerId}")
    public Response songOfSingerId(@PathVariable int singerId) {
        return songService.songOfSingerId(singerId);
    }
}
