package com.popcorn.popcorn.service;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.error.UserErrorCode;
import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.Role;
import com.popcorn.popcorn.domain.dto.FirstSignupDto;
import com.popcorn.popcorn.domain.dto.SecondSignupDto;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.domain.entity.UserInterest;
import com.popcorn.popcorn.repository.UserInterestRepository;
import com.popcorn.popcorn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserInterestRepository userInterestRepository;

    public void signup(FirstSignupDto firstSignupDto, SecondSignupDto secondSignupDto) {
        boolean isUser = userRepository.existsByUsername(firstSignupDto.getUsername());
        boolean isEmail = userRepository.existsByEmail(firstSignupDto.getEmail());
        if(isUser){
            throw new IllegalArgumentException("이미 존재하는 ID");
        }
        if(isEmail) {
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

        for(InterestType interest : secondSignupDto.getInterests()){
            UserInterest userInterest = new UserInterest();
            userInterest.setUserAndInterest(user, interest);
            user.addUserInterest(userInterest);
            userInterestRepository.save(userInterest);
        }

    }

    public boolean isExistUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public String findUserName(String name, String email) {

        UserEntity user = userRepository.findByNameAndEmail(name, email);
        if(user == null){
            return "";
        }
        return user.getUsername();
    }

    public ApiResponse<String> isExistByEmail(String email){
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            return ApiResponse.fail(UserErrorCode.NOT_FOUND_USER);
        }
        return ApiResponse.ok("이메일을 가진 유저가 존재");
    }

    public ApiResponse<String> setPassword(String email, String newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
           return ApiResponse.fail(UserErrorCode.NOT_FOUND_USER);
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return ApiResponse.ok("비밀번호 변경 완료");
    }
}
