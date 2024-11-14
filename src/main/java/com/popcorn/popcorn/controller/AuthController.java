package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.domain.dto.FirstSignupDto;
import com.popcorn.popcorn.domain.dto.LoginRequestDto;
import com.popcorn.popcorn.domain.dto.SecondSignupDto;
import com.popcorn.popcorn.domain.dto.SignupRequestDto;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto){
        try{
            userService.signup(signupRequestDto.getFirstSignupDto(), signupRequestDto.getSecondSignupDto());
            return ResponseEntity.status(HttpStatus.CREATED).body("signup successful");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 사용자");
        }
    }

}
