package com.popcorn.popcorn.service;

import com.popcorn.popcorn.domain.dto.SignupDto;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<UserEntity> login(String userName, String password){

        Optional<UserEntity> user = userRepository.findByUserName(userName);
        if(user.isPresent()){
            if(bCryptPasswordEncoder.matches(password, user.get().getPassword())){
                return user;
            }
        }

        return Optional.empty();
    }

    public void signup(SignupDto signupDto) {
        boolean isUser = userRepository.existsByUserName(signupDto.getUserName());
        if(isUser) return;

        UserEntity user = UserEntity.builder()
                .userName(signupDto.getUserName())
                .password(bCryptPasswordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .build();
        userRepository.save(user);

    }
}
