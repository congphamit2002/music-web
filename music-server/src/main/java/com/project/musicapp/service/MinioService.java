package com.project.musicapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioService {
    String uploadFile(MultipartFile file);
    String uploadImage(MultipartFile file);
    String uploadSongListImage(MultipartFile file);
    String uploadSongImage(MultipartFile file);
    String uploadAvatar(MultipartFile file);
    String upload(MultipartFile file, String prefixFileName);
    String removeFile(String fileName);
    String removeMultipleFile(List<String> fileName);
    InputStream downloadFile(String fileName);
}
