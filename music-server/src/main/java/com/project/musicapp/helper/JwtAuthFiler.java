package com.project.musicapp.helper;

import com.google.gson.Gson;
import com.project.musicapp.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFiler extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

        String token = getJwtToken(request);
        if(jwtProvider.validationToken(token)) {
            //token hợp lệ
            String jsonData = jwtProvider.decodeToken(token);
            System.out.println("Check token " + jsonData);
            Gson gson = new Gson();
            //User user = gson.fromJson(jsonData, User.class);
            User userDetail = (User) userDetailsServiceImpl.loadUserByUsername(jsonData);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            //token kh hop le
            System.out.println("Auth : Login failed");
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        String authenToken =  request.getHeader("Authorization");

        if(StringUtils.hasText(authenToken) && authenToken.contains("Bearer")) {
            String token = authenToken.substring(7);
            return token;
        }
        return null;
    }
}
