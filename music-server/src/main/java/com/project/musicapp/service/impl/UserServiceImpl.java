package com.project.musicapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.project.musicapp.common.Response;
import com.project.musicapp.constant.Constants;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.helper.JwtProvider;
import com.project.musicapp.mapper.UserMapper;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.LoginRequest;
import com.project.musicapp.model.request.UserRequest;
import com.project.musicapp.service.MinioService;
import com.project.musicapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final MinioService minioService;
    private final JwtProvider jwtProvider;

    /*
            TODO: Set password to bcrypt
        */
    @Override
    public Response addUser(UserRequest userRequest) {
        if (this.existUser(userRequest.getUsername())) {
            return Response.warning("Username already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        String password = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt(12));
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
    public Response updateUser(int id, UserRequest userRequest) {
        try {
            User user = this.getUserById(id);
            BeanUtils.copyProperties(userRequest, user, "id", "email", "username", "password", "avator");
            if (userMapper.updateById(user) > 0) {
              return Response.success("Update account success");
            } else {
                return Response.warning("Update failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response updateUserAvatar(int id, MultipartFile file) {
        try {
            User user = this.getUserById(id);
            user.setAvator(Constants.UPLOAD_AVATAR_IMAGE + file.getOriginalFilename());
            if("File uploaded successfully!".equals(minioService.uploadAvatar(file)) && userMapper.updateById(user) > 0) {
                return Response.success("Update avatar successfully", user.getAvator());
            } else {
                return Response.error("Update avatar failed");
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
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

    @Override
    public Response userById(int id) {
        try {
            return Response.success(null, this.getUserById(id));
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public User getUserById(int id) throws DataNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new DataNotFoundException("Can not find user by id " + id);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        System.out.println("IN GET USER  " + username);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Response updatePassword(int id, UserRequest userRequest) {
        try {
            User user = this.getUserById(id);
            if (!this.validateOldPassword(userRequest.getOldPassword(), user.getPassword())) {
                return Response.warning("Old password does not match");
            }
            user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt(12)));
            if (userMapper.updateById(user) > 0) {
                return Response.success("Update password success");
            } else {
                return Response.error("Update failed");
            }
        } catch (DataNotFoundException e) {
            return Response.notFound(e.getMessage());
        } catch (RuntimeException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Boolean validateOldPassword(String passwordRequest, String oldPassword) {
        return BCrypt.checkpw(passwordRequest, oldPassword);
    }
}
