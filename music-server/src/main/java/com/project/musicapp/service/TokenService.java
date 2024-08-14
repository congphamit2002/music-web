package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.model.domain.Token;

import java.util.List;

public interface TokenService extends IService<Token> {

    List<Token> findAllAccessTokensByUser(Integer userId);

    Token findByAccessToken(String token);
}
