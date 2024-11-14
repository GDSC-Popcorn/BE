package com.popcorn.popcorn.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.popcorn.domain.entity.RefreshEntity;
import com.popcorn.popcorn.repository.RefreshRepository;
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

import java.io.IOException;
import java.util.*;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
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

        long accessTokenExpiry = 600000L * 6; // 60분
        long refreshTokenExpiry = 600000L * 6 * 24 * 7; // 7일

        String access = jwtUtil.createJwt("access", username, role, accessTokenExpiry); //60분
        String refresh = jwtUtil.createJwt("refresh", username, role, refreshTokenExpiry); //7일

        addRefreshEntity(username, refresh, 600000L*6*24*7);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access);
        tokens.put("access_expired_at", String.valueOf(System.currentTimeMillis() + accessTokenExpiry));
        tokens.put("refresh_token", refresh);
        tokens.put("refresh_expired_at", String.valueOf(System.currentTimeMillis() + refreshTokenExpiry));


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tokens);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(json);

    }

    private void addRefreshEntity(String username, String refresh, long expiredMs){
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }
}
