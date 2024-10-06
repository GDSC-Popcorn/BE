package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.domain.dto.SignupDto;
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
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto){
        userService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("signup successful");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String userName,
            @RequestParam String password
    ){
        Optional<UserEntity> user = userService.login(userName, password);

        if(user.isPresent()){
            return ResponseEntity.ok("Login successful");
        }
        else{
            return ResponseEntity.status(401).body("Login failed");
        }
    }

}
