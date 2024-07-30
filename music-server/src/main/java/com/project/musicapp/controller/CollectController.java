package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.CollectRequest;
import com.project.musicapp.service.CollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectController {
    private final CollectService collectService;

    @PostMapping("")
    public Response addCollect(@RequestBody CollectRequest collectRequest) {
        return collectService.addCollect(collectRequest);
    }

    @DeleteMapping("")
    public Response deleteCollect(@RequestParam int userId, @RequestParam int songId) {
        return collectService.deleteCollect(userId, songId);
    }

    @GetMapping("/status")
    public Response existSongId(@RequestParam int userId, @RequestParam int songId) {
        return collectService.existSongId(userId, songId);
    }

    @GetMapping("")
    public Response collectOfUser(@RequestParam int userId) {
        return collectService.collectOfUser(userId);
    }
}
