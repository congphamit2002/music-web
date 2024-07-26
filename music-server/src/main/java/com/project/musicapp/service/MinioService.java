package com.project.musicapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadFile(MultipartFile file);
    String uploadImage(MultipartFile file);
    String uploadSongListImage(MultipartFile file);
    String uploadSongImage(MultipartFile file);
    String uploadAvatar(MultipartFile file);
    String upload(MultipartFile file, String prefixFileName);
}
