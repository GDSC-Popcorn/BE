package com.popcorn.popcorn.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCodeIfs{

    NOT_FOUND_USER("fail", 100,"not found user"),
    USER_ALREADY_EXIST_ID("fail",200,"이미 존재하는 ID"),
    USER_ALREADY_EXIST_EMAIL("fail",102,"이미 존재하는 이메일"),
    USER_ALREADY_EXIST_INFO("fail", 200, "이미 존재하는 Oauth 정보"),
    OAUTH_PROVIDER_NOT_MATCH("fail", 200, "provider가 올바르지 않음");


    private final String status;
    private final Integer resultCode;
    private final String message;

}
