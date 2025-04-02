package com.popcorn.popcorn.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsernameDto {

    @NotBlank(message = "username은 필수 값입니다.")
    @Size(min = 4, message = "username은 최소 4자리 이상이어야 합니다.")
    @Pattern(regexp = "[a-zA-Z0-9]+$", message = "ID는 영문 대소문자와 숫자만 포함할 수 있습니다.(특수문자 X)")
    private String username;
}
