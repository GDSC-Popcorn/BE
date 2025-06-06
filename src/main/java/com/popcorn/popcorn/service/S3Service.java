package com.popcorn.popcorn.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.popcorn.popcorn.common.exception.S3UploadExceptioin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.S3.bucket}")
    private String bucket;

    public String reviewImageUpload(MultipartFile multipartFile, Long reviewId) {
        try {
            String key = generateReviewFilename(reviewId);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            s3Client.putObject(new PutObjectRequest(bucket, key, multipartFile.getInputStream(), metadata));
            return key;
        } catch (IOException e) {
            throw S3UploadExceptioin.EXCEPTION;
        }

    }


    public String generateReviewFilename(Long reviewId){
        String fileName = "review" + "/"+ reviewId + "/" + UUID.randomUUID();
        return fileName;
    }

    public String getReviewImageUrl(String fileName) {
        return s3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteImage(String key) {
        s3Client.deleteObject(bucket, key);
    }

}
