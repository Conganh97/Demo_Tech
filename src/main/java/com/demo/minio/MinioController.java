package com.demo.minio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/minio")
@Tag(name = "Minio Controller", description = "Controller for Minio")
public class MinioController {
    private static MinioService minioService;


    @Operation(summary = "Say Hello", description = "Returns a simple greeting message")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        minioService.uploadFile("test", file.getOriginalFilename(), file);
        return ResponseEntity.ok().body("OK");
    }
}
