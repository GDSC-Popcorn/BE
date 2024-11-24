package com.popcorn.popcorn.controller;

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
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        try{
            userService.signup(signupRequestDto.getFirstSignupDto(), signupRequestDto.getSecondSignupDto());
            return ResponseEntity.status(HttpStatus.CREATED).body("signup successful");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 사용자 ID / email");
        }
    }

}
