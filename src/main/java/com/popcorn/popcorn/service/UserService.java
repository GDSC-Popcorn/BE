package com.popcorn.popcorn.service;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.error.UserErrorCode;
import com.popcorn.popcorn.common.exception.UserAlreadyExistException;
import com.popcorn.popcorn.common.exception.UserNotFoundException;
import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.Role;
import com.popcorn.popcorn.domain.dto.*;
import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.domain.entity.UserInterest;
import com.popcorn.popcorn.jwt.JwtUtil;
import com.popcorn.popcorn.oauth.apple.helper.AppleOauthHelper;
import com.popcorn.popcorn.oauth.kakao.helper.KakaoOauthHelper;
import com.popcorn.popcorn.repository.UserInterestRepository;
import com.popcorn.popcorn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserInterestRepository userInterestRepository;
    private final RefreshService refreshService;
    private final KakaoOauthHelper kakaoOauthHelper;
    private final AppleOauthHelper appleOauthHelper;

    public void signup(FirstSignupDto firstSignupDto, SecondSignupDto secondSignupDto) {
        boolean isUser = userRepository.existsByUsername(firstSignupDto.getUsername());
        boolean isEmail = userRepository.existsByEmail(firstSignupDto.getEmail());
        if (isUser) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }
        if (isEmail) {
            throw new IllegalArgumentException("이미 존재하는 이메일");
        }

        UserEntity user = UserEntity.builder()
                .username(firstSignupDto.getUsername())
                .password(bCryptPasswordEncoder.encode(firstSignupDto.getPassword()))
                .nickname(secondSignupDto.getNickname())
                .email(firstSignupDto.getEmail())
                .name(firstSignupDto.getName())
                .role(Role.USER)
                .profileId(secondSignupDto.getProfileId())
                .build();

        userRepository.save(user);

        for (InterestType interest : secondSignupDto.getInterests()) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUserAndInterest(user, interest);
            user.addUserInterest(userInterest);
            userInterestRepository.save(userInterest);
        }

    }


    /*
    oauth로그인 결과 반환
    * */
    public OauthLoginResponse loginUser(OauthInfo oauthInfo) {
        Optional<UserEntity> userOptional = userRepository.findByOauthInfo(oauthInfo);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return generateLoginResponse(user, false);
        } else {
            return OauthLoginResponse.builder()
                    .isNewUser(true)
                    .build();
        }

    }

    private OauthLoginResponse generateLoginResponse(UserEntity user, boolean isNewUser) {
        String access = jwtUtil.createJwt("access", user.getUsername(), user.getRole().toString(), 1);
        String refresh = jwtUtil.createJwt("refresh", user.getUsername(), user.getRole().toString(), 24 * 7);

        String accessExpiry = jwtUtil.getExpiryFormatted(access);
        String refreshExpiry = jwtUtil.getExpiryFormatted(refresh);

        refreshService.addRefreshEntity(user.getUsername(), refresh, 24 * 7);

        return OauthLoginResponse.builder()
                .isNewUser(isNewUser)
                .access(access)
                .accessExpiredAt(accessExpiry)
                .refresh(refresh)
                .refreshExpiredAt(refreshExpiry)
                .build();

    }


    public UserEntity queryUserByOauthInfo(OauthInfo oauthInfo) {
        return userRepository
                .findByOauthInfo(oauthInfo)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    public boolean isExistUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public String findUserName(String name, String email) {

        UserEntity user = userRepository.findByNameAndEmail(name, email);
        if (user == null) {
            return "";
        }
        return user.getUsername();
    }

    public ApiResponse<String> isExistByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ApiResponse.ok("이메일 사용 가능");
        }
        return ApiResponse.fail(200, "fail", "이메일을 가진 유저가 존재");
    }

    public ApiResponse<String> setPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return ApiResponse.fail(UserErrorCode.NOT_FOUND_USER);
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return ApiResponse.ok("비밀번호 변경 완료");
    }

    public OauthLoginResponse signupKakaoWhenFirstOauthLogin(AfterOauthSignupDto afterOauthSignupDto) {
        OauthInfo oauthInfo = kakaoOauthHelper.getOauthInfoByKakaoIdToken(afterOauthSignupDto.getIdToken());
        //이미 있는 유저면 에러
        Optional<UserEntity> byOauthInfo = userRepository.findByOauthInfo(oauthInfo);
        if(byOauthInfo.isPresent()){
            throw UserAlreadyExistException.EXCEPTION;
        }
        UserEntity user = UserEntity.builder()
                .role(Role.USER)
                .oauthInfo(oauthInfo)
                .username("user" + UUID.randomUUID())
                .build();
        userRepository.save(user);
        return MatchingUserProcess(afterOauthSignupDto, user);
    }

    //중복이 있어서 분기를 나눌까 생각했지만 사실 애플, 카카오 로그인밖에 없기때문에 그냥 이대로 진행.
    public OauthLoginResponse signupAppleWhenFirstOauthLogin(AfterOauthSignupDto afterOauthSignupDto) {
        OauthInfo oauthInfo = appleOauthHelper.getOauthInfoByAppleIdToken(afterOauthSignupDto.getIdToken());

        //이미 있는 유저면 에러
        Optional<UserEntity> byOauthInfo = userRepository.findByOauthInfo(oauthInfo);
        if(byOauthInfo.isPresent()){
            throw UserAlreadyExistException.EXCEPTION;
        }

        UserEntity user = UserEntity.builder()
                .role(Role.USER)
                .oauthInfo(oauthInfo)
                .username("user" + UUID.randomUUID())
                .build();
        userRepository.save(user);
        return MatchingUserProcess(afterOauthSignupDto, user);

    }

    private OauthLoginResponse MatchingUserProcess(AfterOauthSignupDto afterOauthSignupDto, UserEntity user) {

        user.updateUserInfo(afterOauthSignupDto.getSecondSignupDto().getNickname(), afterOauthSignupDto.getSecondSignupDto().getProfileId());


        for (InterestType interest : afterOauthSignupDto.getSecondSignupDto().getInterests()) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUserAndInterest(user, interest);
            user.addUserInterest(userInterest);
            userInterestRepository.save(userInterest);
        }
        return generateLoginResponse(user, true);

    }

}
