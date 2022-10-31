package com.cosmost.project.cosmost.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cosmost.project.cosmost.requestbody.FileInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
@Component
public class AmazonS3ResourceStorage {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Autowired
    public AmazonS3ResourceStorage(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void store(FileInfoRequest fileInfoRequest, MultipartFile multipartFile) {
        File local = new File(MultipartUtil.getBaseDir());

        if(!local.exists()) {
            local.mkdir();
        }

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