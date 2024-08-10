package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.Comment;
import com.project.musicapp.model.request.CommentRequest;

public interface CommentService extends IService<Comment> {
    Response addComment(CommentRequest commentRequest);

    Response updateCommentMsg(int id, CommentRequest commentRequest);

    Response deleteComment(int id);

    Response commentOfSongId(int songId);

    Response commentOfSongListId(int songListId);

    Comment getCommentById(int id) throws DataNotFoundException;
}
