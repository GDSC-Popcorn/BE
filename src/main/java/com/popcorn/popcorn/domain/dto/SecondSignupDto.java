package com.popcorn.popcorn.domain.dto;

import com.popcorn.popcorn.domain.InterestType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SecondSignupDto {

    @NotEmpty(message = "nickname is required")
    private String nickname;

    @NotNull
    private List<InterestType> interests;

}
