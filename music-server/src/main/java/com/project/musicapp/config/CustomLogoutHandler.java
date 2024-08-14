package com.project.musicapp.config;

import com.project.musicapp.mapper.TokenMapper;
import com.project.musicapp.model.domain.Token;
import com.project.musicapp.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenService tokenService;
    private final TokenMapper tokenMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
            return;
        }

        String token = authHeader.substring(7);
        Token storedToken = tokenService.findByAccessToken(token);

        if (storedToken != null && !storedToken.getIsLoggedOut()) {
            storedToken.setIsLoggedOut(true);
            tokenMapper.updateById(storedToken);
            try {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\":\"200\",\"message\":\"Log out successfully\"}");
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            try {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"400\",\"message\":\"Invalid token\"}");
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
}
