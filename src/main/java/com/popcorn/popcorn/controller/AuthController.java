package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.error.UserErrorCode;
import com.popcorn.popcorn.domain.dto.*;
import com.popcorn.popcorn.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static com.popcorn.popcorn.common.error.UserErrorCode.NOT_FOUND_USER;
import static com.popcorn.popcorn.common.error.UserErrorCode.USER_ALREADY_EXIST_ID;

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
                        .body(ApiResponse.fail(USER_ALREADY_EXIST_ID));
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

    @GetMapping("/chkUser")
    public ResponseEntity<ApiResponse<String>> chkUser(@RequestBody @Valid UsernameDto usernameDto){
        boolean isExist = userService.isExistUsername(usernameDto.getUsername());
        if(!isExist){
            return ResponseEntity.ok(ApiResponse.ok("사용 가능한 ID"));
        } else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(USER_ALREADY_EXIST_ID));
        }
    }


    @GetMapping("/validEmail")
    public ResponseEntity<ApiResponse<String>> isExistByEmail(@RequestBody @Valid EmailRequestDto emailRequestDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.isExistByEmail(emailRequestDto.getEmail()));
    }

    @GetMapping("/finduser")
    public ResponseEntity<ApiResponse<String>> finduser(@RequestBody @Valid FindNameDto findNameDto){
        String name = userService.findUserName(findNameDto.getName(), findNameDto.getEmail());
        if(Objects.equals(name, "")){
            return ResponseEntity.ok(ApiResponse.fail(NOT_FOUND_USER));
        }
        return ResponseEntity.ok(ApiResponse.ok(name));
    }

    @PostMapping("/setPassword")
    public ApiResponse<String> setpassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto){
        return userService.setPassword(passwordRequestDto.getEmail(), passwordRequestDto.getPassword());
    }





}
