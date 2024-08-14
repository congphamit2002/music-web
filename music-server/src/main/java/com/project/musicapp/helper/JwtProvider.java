package com.project.musicapp.helper;

import com.google.gson.Gson;
import com.project.musicapp.model.domain.Token;
import com.project.musicapp.service.TokenService;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final TokenService tokenService;

    //Khai báo hàm tạo token
    //Giải mã token
    //Kiểm tra token có phải do hệ thống sinh ra hay không

    @Value("${security.secret_key}")
    private String SECRET_KEY;
    @Value("${security.jwt_expired}")
    private long JWT_EXPIRED;
    private Gson gson = new Gson();

    public String generateToken(String data) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + JWT_EXPIRED);
        return Jwts.builder()
                .setSubject(data) //Dữ liệu muốn lưu kèm ở token
                .setIssuedAt(now) //thời gian khởi tạo
                .setExpiration(expired) //thời gian hết hạn
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Thuật toán mã hóa và mã hóa dựa trên Secert key
                .compact();
    }

    public String decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)//Key max hóa token
                .build()
                .parseClaimsJws(token)  //truyền tham số token
                .getBody().getSubject(); //lấy giá trị
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()//Key max hóa token
                    .parseClaimsJws(token);
            Token result = tokenService.findByAccessToken(token);
            if (result == null || result.getIsLoggedOut()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
}
