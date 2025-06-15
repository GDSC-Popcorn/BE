package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;

public class S3UploadExceptioin extends PopcornException {
    public static final PopcornException EXCEPTION = new S3UploadExceptioin();


    public S3UploadExceptioin() {

        super(ErrorCode.S3_UPLOAD_FAILED);
    }
}
