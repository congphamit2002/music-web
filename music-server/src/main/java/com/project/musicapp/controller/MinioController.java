package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class MinioController {
    private final MinioService minioService;

    @PostMapping("/test-upload")
    public Response testUpload(@RequestParam("file") MultipartFile file) {
        return Response.success(minioService.uploadFile(file));
    }
}
