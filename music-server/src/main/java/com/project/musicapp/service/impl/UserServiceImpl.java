package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.musicapp.common.Response;
import com.project.musicapp.constant.Constants;
import com.project.musicapp.mapper.UserMapper;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.UserRequest;
import com.project.musicapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    /*
        TODO: Set password to bcrypt
    */
    @Override
    public Response addUser(UserRequest userRequest) {
        if (this.existUser(userRequest.getUsername())) {
            return Response.warning("Username already exists");
        }
        System.out.println("Request " + userRequest.toString());
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        String password = DigestUtils.md5DigestAsHex((Constants.SALT + userRequest.getPassword()).getBytes(StandardCharsets.UTF_8));
        user.setPassword(password);
        if ("".equals(userRequest.getEmail())) {
            user.setEmail(null);
        }
        if (StringUtils.isBlank(userRequest.getPhoneNum())) {
            user.setPhoneNum(null);
        }
        user.setAvator("img/avatorImages/user.jpg");
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", user.getEmail());
            User record = userMapper.selectOne(queryWrapper);
            if (record != null) {
                return Response.warning("Email already exists");
            }
            if (userMapper.insert(user) > 0) {
                return Response.success("Register account sucess");
            } else {
                return Response.warning("Register failed");
            }
        } catch (DuplicateKeyException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public boolean existUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Long count = userMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
