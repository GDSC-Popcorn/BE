package com.popcorn.popcorn.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //JSON형식으로 받아올꺼니까
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getReader(), Map.class);

            String username = credentials.get("username");
            String password = credentials.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e){
            throw new AuthenticationException("Failed to parse authentication request") {};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //json형식으로 반환하도록.

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, 1); //60분
        String refresh = jwtUtil.createJwt("refresh", username, role, 24*7); //7일

        redisUtil.storeRefreshToken(username, refresh, 24*7);
        //addRefreshEntity(username, refresh, 24*7);

        // 만료 시간 계산
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime accessExpiration = now.plusHours(1);
        ZonedDateTime refreshExpiration = now.plusHours(24*7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access);
        tokens.put("access_expired_at", accessExpiration.format(formatter));
        tokens.put("refresh_token", refresh);
        tokens.put("refresh_expired_at", refreshExpiration.format(formatter));

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.ok(tokens);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(json);

    }

//    private void addRefreshEntity(String username, String refresh, long plusHour){
//        LocalDateTime expirationDate = LocalDateTime.now().plusHours(plusHour);
//
//        RefreshEntity refreshEntity = RefreshEntity.builder()
//                .username(username)
//                .refresh(refresh)
//                .expiration(expirationDate)
//                .build();
//
//        refreshRepository.save(refreshEntity);
//    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ApiResponse<String> apiResponse = ApiResponse.fail(
                HttpStatus.UNAUTHORIZED.value(),
                "fail",
                "Invalid username or password"
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(apiResponse);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(json);

    }
}
