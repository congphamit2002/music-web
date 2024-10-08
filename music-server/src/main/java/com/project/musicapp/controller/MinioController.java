package com.project.musicapp.controller;

import com.project.musicapp.common.Response;
import com.project.musicapp.service.MinioService;
import io.minio.GetObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String fileName) {
        try {
            InputStream fileStream = minioService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<byte[]> getFile(@RequestParam String fileName) throws Exception {
        InputStream stream = minioService.downloadFile(fileName);

        byte[] bytes = IOUtils.toByteArray(stream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
