package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.jwt.JwtUtil;
import com.popcorn.popcorn.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ReissueController {

    private final RedisUtil redisUtil;
    @Value("${spring.jwt.access.plus-hour}")
    private long accessTokenPlusHour;

    @Value("${spring.jwt.refresh.plus-hour}")
    private long refreshTokenPlusHour;

    private final JwtUtil jwtUtil;


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        String refresh = request.getHeader("X-refresh-token");

        if(refresh == null){
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>("만료된 리프레쉬 토큰", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){
            return new ResponseEntity<>("리프레쉬 토큰이 아님", HttpStatus.BAD_REQUEST);
        }



        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String storedRefresh = redisUtil.getData(username);
        if(storedRefresh == null || !storedRefresh.equals(refresh)){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        String newAccess = jwtUtil.createJwt("access", username, role, accessTokenPlusHour);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, refreshTokenPlusHour);

        redisUtil.storeRefreshToken(username, newRefresh, refreshTokenPlusHour);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime accessExpiration = now.plusHours(1);
        ZonedDateTime refreshExpiration = now.plusHours(24*7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("new_access_token", newAccess);
        tokens.put("new_access_expired_at", accessExpiration.format(formatter));
        tokens.put("new_refresh_token", refresh);
        tokens.put("new_refresh_expired_at", refreshExpiration.format(formatter));

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.ok(tokens);

        response.setHeader("access", newAccess);
        response.setHeader("X-refresh-token", newRefresh);

        return ResponseEntity.ok(apiResponse);
    }

//    private void addRefreshEntity(String username, String refresh, long plusHour){
//        LocalDateTime expirationDate = LocalDateTime.now().plusHours(plusHour);
//
//        RefreshEntity refreshEntity = RefreshEntity.builder()
//                .refresh(refresh)
//                .username(username)
//                .expiration(expirationDate)
//                .build();
//
//        refreshRepository.save(refreshEntity);
//    }




}
