package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.mapper.TokenMapper;
import com.project.musicapp.model.domain.Token;
import com.project.musicapp.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements TokenService {
    private final TokenMapper tokenMapper;

    @Override
    public List<Token> findAllAccessTokensByUser(Integer userId) {
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return tokenMapper.selectList(queryWrapper);
    }

    @Override
    public Token findByAccessToken(String token) {
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("access_token", token);
        return tokenMapper.selectOne(queryWrapper);
    }
}
