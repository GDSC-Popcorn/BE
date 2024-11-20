package com.popcorn.popcorn.service;

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

import javax.lang.model.type.ErrorType;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserInterestRepository userInterestRepository;

    public void signup(FirstSignupDto firstSignupDto, SecondSignupDto secondSignupDto) {
        boolean isUser = userRepository.existsByUsername(firstSignupDto.getUsername());
        if(isUser){
            throw new IllegalArgumentException("이미 존재하는 사용자");
        }

        UserEntity user = UserEntity.builder()
                .username(firstSignupDto.getUsername())
                .password(bCryptPasswordEncoder.encode(firstSignupDto.getPassword()))
                .nickname(secondSignupDto.getNickname())
                .email(firstSignupDto.getEmail())
                .name(firstSignupDto.getName())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        for(InterestType interest : secondSignupDto.getInterests()){
            UserInterest userInterest = new UserInterest();
            userInterest.setUserAndInterest(user, interest);
            user.addUserInterest(userInterest);
            userInterestRepository.save(userInterest);
        }

    }
}
