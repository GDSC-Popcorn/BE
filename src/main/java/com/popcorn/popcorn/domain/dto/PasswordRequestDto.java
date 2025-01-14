package com.popcorn.popcorn.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordRequestDto {

    @NotBlank(message = "null, \"\", \" \" 허용되지 않습니다.")
    private String email;

    @NotBlank(message = "null, \"\", \" \" 허용되지 않습니다.")
    private String password;
}
