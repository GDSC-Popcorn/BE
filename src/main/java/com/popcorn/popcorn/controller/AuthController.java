package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.error.ErrorCode;
import com.popcorn.popcorn.common.error.UserErrorCode;
import com.popcorn.popcorn.domain.dto.SignupRequestDto;
import com.popcorn.popcorn.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(){
        return ResponseEntity.ok().body(Map.of("status","hello_world"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        try {
            userService.signup(signupRequestDto.getFirstSignupDto(), signupRequestDto.getSecondSignupDto());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("회원가입 완료"));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("이미 존재하는 ID")) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail(UserErrorCode.USER_ALREADY_EXIST_ID));
            } else if (e.getMessage().contains("이미 존재하는 이메일")) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail(UserErrorCode.USER_ALREADY_EXIST_EMAIL));
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail(UserErrorCode.USER_ALREADY_EXIST_EMAIL));
            }
        }
    }

}
