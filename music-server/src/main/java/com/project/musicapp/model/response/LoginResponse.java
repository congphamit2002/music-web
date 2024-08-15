package com.project.musicapp.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer userId;

    private String userName;

    private String avatar;

    private String accessToken;
}
