package com.popcorn.popcorn.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FirstSignupDto {

    private String username;

    private String password;

    private String name;

    private String email;

}
