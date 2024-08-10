package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.Comment;
import com.project.musicapp.model.request.CommentRequest;
import com.project.musicapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public Response addComment(@RequestBody CommentRequest commentRequest) {
        return commentService.addComment(commentRequest);
    }

    @PutMapping("/{id}")
    public Response updateComment(@PathVariable int id, @RequestBody CommentRequest commentRequest) {
        return commentService.updateCommentMsg(id, commentRequest);
    }

    @DeleteMapping("/{id}")
    public Response deleteComment(@PathVariable int id) {
        return commentService.deleteComment(id);
    }

    @GetMapping("/songs/{songId}")
    public Response getCommentBySongId(@PathVariable int songId) {
        return commentService.commentOfSongId(songId);
    }

    @GetMapping("/song-lists/{songListId}")
    public Response getCommentBySongListId(@PathVariable int songListId) {
        return commentService.commentOfSongListId(songListId);
    }
}
