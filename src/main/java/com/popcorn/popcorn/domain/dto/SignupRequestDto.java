package com.popcorn.popcorn.domain.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignupRequestDto {

    @Valid
    private FirstSignupDto firstSignupDto;

    @Valid
    private SecondSignupDto secondSignupDto;
}
