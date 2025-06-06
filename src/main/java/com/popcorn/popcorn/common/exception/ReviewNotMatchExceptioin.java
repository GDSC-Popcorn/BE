package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;

public class ReviewNotMatchExceptioin extends PopcornException {
    public static final PopcornException EXCEPTION = new ReviewNotMatchExceptioin();


    public ReviewNotMatchExceptioin() {
        super(ErrorCode.REVIEW_NOT_MATCH);
    }
}
