package com.project.musicapp.service.impl;

import com.project.musicapp.constant.Constants;
import com.project.musicapp.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
        return upload(file, Constants.UPLOAD_SINGER_IMAGE);
    }

    @Override
    public String uploadSongListImage(MultipartFile file) {
        return upload(file, Constants.UPLOAD_SONG_LIST);
    }

    @Override
    public String uploadSongImage(MultipartFile file) {
        return upload(file, Constants.UPLOAD_SONG_IMAGE
        );
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        return upload(file, Constants.UPLOAD_AVATAR_IMAGE);
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
                            .object(prefixFileName + file.getOriginalFilename())
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

    @Override
    public String removeFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return "File removed successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String removeMultipleFile(List<String> fileName) {
        try {
            List<DeleteObject> deleteObjects = fileName.stream()
                    .map(DeleteObject::new)
                    .toList();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(deleteObjects)
                            .build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                return "Error deleting object " + error.objectName() + "; " + error.message();
            }
            return "File removed successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public InputStream downloadFile(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName).build()
            );
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException | RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
