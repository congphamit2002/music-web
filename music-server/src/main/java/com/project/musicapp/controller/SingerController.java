package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.request.SingerRequest;
import com.project.musicapp.service.SingerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/singers")
@RequiredArgsConstructor
public class SingerController {
    private final SingerService singerService;

    @GetMapping("")
    public Response getAllSingers() {
        return singerService.allSinger();
    }

    @GetMapping("/sex")
    public Response singerBySex(@RequestParam Integer sex) {
        return singerService.singerBySex(sex);
    }

    @GetMapping("/name")
    public Response singerByName(@RequestParam String name) {
        return singerService.singerByName(name);
    }

    @PostMapping("")
    public Response addSinger(@RequestBody SingerRequest singerRequest) {
        return singerService.addSinger(singerRequest);
    }

    @PutMapping("/{id}")
    public Response updateSinger(@PathVariable Integer id, @RequestBody SingerRequest singerRequest) {
        return singerService.updateSinger(id, singerRequest);
    }

    @DeleteMapping("/{id}")
    public Response deleteSinger(@PathVariable Integer id) {
        return singerService.deleteSinger(id);
    }

    @PostMapping("/{id}/avatar")
    public Response updateSingerAvatar(@PathVariable Integer id, @RequestParam MultipartFile file) {
        return singerService.updateSingerAvatar(id, file);
    }
}
