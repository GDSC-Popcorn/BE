package com.popcorn.popcorn.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AfterOauthSignupDto {

    private String idToken;

    private SecondSignupDto secondSignupDto;
}
