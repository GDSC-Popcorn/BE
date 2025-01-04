package com.popcorn.popcorn.common.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.popcorn.popcorn.common.error.ErrorCode;
import com.popcorn.popcorn.common.error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer resultCode;

    private String status;

    private T data;

    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(ErrorCode.OK.getResultCode(), "success", data);
    }

    public static ApiResponse<String> fail(ErrorCodeIfs errorCode){
        return new ApiResponse<>(errorCode.getResultCode(), errorCode.getStatus(), errorCode.getMessage());
    }

    public static <T> ApiResponse<T> fail(Integer code, String status, T data){
        return new ApiResponse<>(code, status, data);
    }
}
