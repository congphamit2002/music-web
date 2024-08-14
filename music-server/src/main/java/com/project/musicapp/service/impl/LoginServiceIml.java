package com.project.musicapp.service.impl;

import com.google.gson.Gson;
import com.project.musicapp.common.Response;
import com.project.musicapp.helper.JwtProvider;
import com.project.musicapp.mapper.TokenMapper;
import com.project.musicapp.model.domain.Token;
import com.project.musicapp.model.domain.User;
import com.project.musicapp.model.request.LoginRequest;
import com.project.musicapp.service.LoginService;
import com.project.musicapp.service.TokenService;
import com.project.musicapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginServiceIml implements LoginService {
    private final TokenMapper tokenMapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public Response login(LoginRequest loginRequest) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest);
            //Hàm dùng để kích hoạt đăng nhập bằng tay
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                            , loginRequest.getPassword()) );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtProvider.generateToken(loginRequest.getUsername());
            User user = userService.getUserByUsername(loginRequest.getUsername());
            revokeAllTokenByUser(user);
            saveUserToken(jwtToken, user);
            return Response.success(null, jwtToken);
        } catch (UsernameNotFoundException e) {
            // Handle case where the user is not found
            System.err.println("User not found: " + e.getMessage());
            return Response.warning("Username not found");
        } catch (BadCredentialsException e) {
            // Handle case where the credentials are incorrect
            System.err.println("Invalid credentials: " + e.getMessage());
            return Response.warning("Invalid username or password");
        } catch (Exception e) {
            // Handle any other exceptions
            System.err.println("Login error: " + e.getMessage());
            return Response.warning("Authentication failed");
        }
    }

    @Override
    public void saveUserToken(String accessToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setUserId(user.getId());
        token.setIsLoggedOut(false);
        tokenMapper.insert(token);
    }

    @Override
    public void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenService.findAllAccessTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach((token) -> {
            token.setIsLoggedOut(true);
            tokenMapper.updateById(token);
        });

    }
}
