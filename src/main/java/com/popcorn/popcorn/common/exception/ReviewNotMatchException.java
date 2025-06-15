package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;

public class ReviewNotMatchException extends PopcornException {
    public static final PopcornException EXCEPTION = new ReviewNotMatchException();


    public ReviewNotMatchException() {
        super(ErrorCode.REVIEW_NOT_MATCH);
    }
}
