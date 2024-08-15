package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.mapper.UserSupportMapper;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.domain.UserSupport;
import com.project.musicapp.model.request.UserSupportRequest;
import com.project.musicapp.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSupportServiceImpl extends ServiceImpl<UserSupportMapper, UserSupport> implements UserSupportService {
    private final UserSupportMapper userSupportMapper;

    @Override
    public Response likeComment(UserSupportRequest userSupportRequest) {
        try {
            if (this.getUserSupportByUserIdAndCommentId(userSupportRequest.getUserId(), userSupportRequest.getCommentId()) != null) {
                return Response.warning("User is existed like this comment");
            }
            UserSupport userSupport = new UserSupport();
            BeanUtils.copyProperties(userSupportRequest, userSupport, "id");
            if (userSupportMapper.insert(userSupport) > 0) {
                return Response.success("Like comment success");
            } else {
                return Response.error("Like comment failed");
            }
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response unlikeComment(UserSupportRequest userSupportRequest) {
        try {
            UserSupport userSupport = this.getUserSupportByUserIdAndCommentId(userSupportRequest.getUserId(), userSupportRequest.getCommentId());
            if (userSupport == null) {
                return Response.notFound("Can not find comment support by user id: " + userSupportRequest.getUserId() + " and comment id: " + userSupportRequest.getCommentId());
            }
            if (userSupportMapper.deleteById(userSupport) > 0) {
                return Response.success("Unlike comment success");
            } else {
                return Response.error("Unlike comment fail");
            }
        }catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response existLikeComment(int commentId, int userId) {
        try {
            UserSupport userSupport = this.getUserSupportByUserIdAndCommentId(userId, commentId);
            if (userSupport == null) {
                return Response.success(null, false);
            }
            return Response.success(null, true);
        }catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public UserSupport getUserSupportByUserIdAndCommentId(int userId, int commentId) {
        QueryWrapper<UserSupport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("comment_id", commentId);
        return userSupportMapper.selectOne(queryWrapper);
    }
}
