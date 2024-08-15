package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.CommentMapper;
import com.project.musicapp.model.domain.Comment;
import com.project.musicapp.model.domain.Singer;
import com.project.musicapp.model.request.CommentRequest;
import com.project.musicapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final CommentMapper commentMapper;

    @Override
    public Response addComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentRequest, comment);
        comment.setType(commentRequest.getNowType());
        try {
            if (commentMapper.insert(comment) > 0) {
                return Response.success("Insert comment success");
            } else {
                return Response.error("Insert comment failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateCommentMsg(int id, CommentRequest commentRequest) {
        try {
            Comment comment = this.getCommentById(id);
            BeanUtils.copyProperties(commentRequest, comment, "id");
            if(commentMapper.updateById(comment) > 0) {
                return Response.success("Update comment success");
            } else {
                return Response.error("Update comment failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response deleteComment(int id) {
        try {
            Comment comment = this.getCommentById(id);
            if(commentMapper.deleteById(id) > 0) {
                return Response.success("Delete comment success");
            } else {
                return Response.error("Delete comment failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response commentOfSongId(int songId) {
        try {
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("song_id", songId);
            commentMapper.selectList(queryWrapper).stream().forEach(System.out::println);
            return Response.success(null, commentMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response commentOfSongListId(int songListId) {
        try {
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("song_list_id", songListId);
            return Response.success(null, commentMapper.selectList(queryWrapper));
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Comment getCommentById(int id) throws DataNotFoundException{
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Comment comment = commentMapper.selectOne(queryWrapper);
        if (comment == null) {
            throw new DataNotFoundException("Can not find comment by id " + id);
        }
        return comment;
    }
}
