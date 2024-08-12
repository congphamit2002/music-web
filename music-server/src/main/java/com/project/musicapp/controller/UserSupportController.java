package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.UserSupportRequest;
import com.project.musicapp.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("userSupports")
public class UserSupportController {
    private final UserSupportService userSupportService;

    @GetMapping("/exist")
    public Response existLikeComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.existLikeComment(userSupportRequest);
    }

    @PostMapping("/like")
    public Response likeComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.likeComment(userSupportRequest);
    }

    @PostMapping("/unlike")
    public Response unlikeComment(@RequestBody UserSupportRequest userSupportRequest) {
        return userSupportService.unlikeComment(userSupportRequest);
    }
}
