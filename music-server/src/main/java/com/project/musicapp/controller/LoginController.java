package com.project.musicapp.controller;

import com.google.gson.Gson;
import com.project.musicapp.common.Response;
import com.project.musicapp.helper.JwtProvider;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.LoginRequest;
import com.project.musicapp.service.LoginService;
import com.project.musicapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest){
        return loginService.login(loginRequest);
    }
}
