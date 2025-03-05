package com.popcorn.popcorn.oauth.apple.controller;

import com.popcorn.popcorn.domain.dto.AfterOauthSignupDto;
import com.popcorn.popcorn.domain.dto.OauthLoginResponse;
import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.oauth.apple.helper.AppleOauthHelper;
import com.popcorn.popcorn.oauth.kakao.dto.OauthLoginRequest;
import com.popcorn.popcorn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AppleLoginController {

    private final AppleOauthHelper appleOauthHelper;
    private final UserService userService;


    @PostMapping("/oauth/apple")
    public ResponseEntity<OauthLoginResponse> loginOauth(@RequestBody OauthLoginRequest request){
        OauthInfo oauthInfo = appleOauthHelper.getOauthInfoByAppleIdToken(request.getIdToken());
        return ResponseEntity.ok(userService.loginUser(oauthInfo));
    }


    @PostMapping("/oauth/apple/signup")
    public ResponseEntity<OauthLoginResponse> signupWhenFirstOauthLogin(@RequestBody AfterOauthSignupDto afterOauthSignupDto) {
        return ResponseEntity.ok(userService.signupAppleWhenFirstOauthLogin(afterOauthSignupDto));
    }

}
