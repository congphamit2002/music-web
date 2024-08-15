package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.request.UserSupportRequest;
import com.project.musicapp.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userSupports")
public class UserSupportController {
    private final UserSupportService userSupportService;

    @GetMapping("/exist")
    public Response existLikeComment(@RequestParam int commentId, @RequestParam int userId) {
        return userSupportService.existLikeComment(commentId, userId);
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
