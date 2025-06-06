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
    NOT_ACCESS("fail", 403, "엑세스 할 수 없음"),

    TOKEN_EXPIRED("fail", 401, "만료된 토큰"),
    INVALID_TOKEN("fail", 401,"잘못된 토큰, 재 로그인해주세요"),

    POPUP_NOT_FOUND("fail", 202, "존재하지 않는 팝업"),
    REVIEW_NOT_FOUND("fail", 202, "존재하지 않는 리뷰"),
    S3_UPLOAD_FAILED("fail", 500, "s3에 업로드 실패"),
    REVIEW_NOT_MATCH("fail", 205, "리뷰 글쓴이가 일치 하지 않음");

    private final String status;
    private final Integer resultCode;
    private final String message;

}
