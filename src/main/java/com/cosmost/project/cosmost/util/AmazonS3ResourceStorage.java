package com.cosmost.project.cosmost.util;

import com.amazonaws.services.s3.AmazonS3;
import com.cosmost.project.cosmost.requestbody.FileInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public void store(FileInfoRequest fileInfoRequest, MultipartFile multipartFile) {
        File file = new File(fileInfoRequest.getLocalPath());

        try {
            multipartFile.transferTo(file);

            amazonS3.putObject(bucket, fileInfoRequest.getRemotePath(), file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(file.exists())
                file.delete();
        }
    }
}