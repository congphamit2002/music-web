package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.UserSupport;
import com.project.musicapp.model.request.UserSupportRequest;

public interface UserSupportService extends IService<UserSupport> {
    Response likeComment(UserSupportRequest userSupportRequest);
    Response unlikeComment(UserSupportRequest userSupportRequest);
    Response existLikeComment(int commentId, int userId);
    UserSupport getUserSupportByUserIdAndCommentId(int userId, int commentId);
}
