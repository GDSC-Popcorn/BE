package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;
import com.popcorn.popcorn.common.error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PopcornException extends RuntimeException{
    private ErrorCodeIfs error;

    public ErrorCodeIfs getError(){
        return this.error;
    }
}
