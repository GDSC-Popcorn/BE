package com.popcorn.popcorn.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs{

    OK("success", 200, "ok"),
    CREATED("success", 201, "created"),
    BAD_REQUEST("fail", 400, "잘못된 요청"),
    SERVER_ERROR("fail",500,"서버에러"),
    NULL_POINT("fail", 512, "NUll Point"),
    NOT_ACCESS("fail", 403, "엑세스 할 수 없음");


    private final String status;
    private final Integer resultCode;
    private final String message;

}
