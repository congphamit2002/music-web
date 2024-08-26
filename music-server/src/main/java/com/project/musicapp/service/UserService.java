package com.project.musicapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.musicapp.common.Response;
import com.project.musicapp.exception.DataNotFoundException;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.LoginRequest;
import com.project.musicapp.model.request.UserRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    Response addUser(UserRequest userRequest);
    Response updateUser(int id, UserRequest userRequest);
    Response updateUserAvatar(int id, MultipartFile file);
    boolean existUser(String username);
    Response userById(int id);
    User getUserById(int id) throws DataNotFoundException;
    User getUserByUsername(String username);
    Response updatePassword(int id, UserRequest userRequest);
    Boolean validateOldPassword(String passwordRequest, String oldPassword);
}
