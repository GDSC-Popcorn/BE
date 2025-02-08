package com.popcorn.popcorn.oauth.kakao.controller;

import com.popcorn.popcorn.domain.dto.AfterOauthSignupDto;
import com.popcorn.popcorn.domain.dto.OauthLoginResponse;
import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.oauth.kakao.dto.OauthLoginRequest;
import com.popcorn.popcorn.oauth.kakao.helper.KakaoOauthHelper;
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
public class KakaoLoginController {

    private final UserService userService;
    private final KakaoOauthHelper kakaoOauthHelper;


    /*
    * 로그인하는 부분
    * */
    @PostMapping("/oauth/kakao")
    public ResponseEntity<OauthLoginResponse> loginOauth(@RequestBody OauthLoginRequest request){
        OauthInfo oauthInfo = kakaoOauthHelper.getOauthInfoByKakaoIdToken(request.getIdToken());
        return ResponseEntity.ok(userService.loginUser(oauthInfo));
    }

    /*
    * 새로운 소셜로그인유저일경우 회원가입2진행후 토큰 발급
    * */
    @PostMapping("/oauth/kakao/signup")
    public ResponseEntity<OauthLoginResponse> signupWhenFirstOauthLogin(@RequestBody AfterOauthSignupDto afterOauthSignupDto) {
        return ResponseEntity.ok(userService.signupKakaoWhenFirstOauthLogin(afterOauthSignupDto));
    }
    


}
