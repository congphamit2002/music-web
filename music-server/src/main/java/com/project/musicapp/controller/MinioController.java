package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class MinioController {
    private final MinioService minioService;

    @PostMapping("/test-upload")
    public Response testUpload(@RequestParam("file") MultipartFile file) {
        return Response.success(minioService.uploadFile(file));
    }

    @DeleteMapping("/test-remove/{fileName}")
    public Response testRemove(@PathVariable String fileName) {
        return Response.success(minioService.removeFile(fileName));
    }

    @DeleteMapping("/test-remove")
    public Response testRemove(@RequestParam List<String> fileNames) {
        return Response.success(minioService.removeMultipleFile(fileNames));
    }
}
