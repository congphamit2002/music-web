package com.project.musicapp.service.impl;

import com.project.musicapp.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        return upload(file, "");
    }

    @Override
    public String uploadImage(MultipartFile file) {
        return "";
    }

    @Override
    public String uploadSongListImage(MultipartFile file) {
        return "";
    }

    @Override
    public String uploadSongImage(MultipartFile file) {
        return "";
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        return "";
    }

    @Override
    public String upload(MultipartFile file, String prefixFileName) {
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                // Make a new bucket if it doesn't exist
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
