package com.project.musicapp.service;

import com.project.musicapp.common.Response;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.LoginRequest;

public interface LoginService {
    Response login(LoginRequest loginRequest);
    void saveUserToken(String accessToken, User user);
    void revokeAllTokenByUser(User user);
}
