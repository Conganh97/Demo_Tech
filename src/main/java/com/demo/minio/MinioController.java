package com.demo.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/minio")
@Tag(name = "Minio Controller", description = "Controller for Minio")
public class MinioController {
    private static MinioService minioService;
    private static MinioClient minioClient;

    @Operation(summary = "Say Hello", description = "Returns a simple greeting message")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        minioService.uploadFile("test", file.getOriginalFilename(), file);
        return ResponseEntity.ok().body("OK");
    }

    public static void main(String[] args) throws Exception {
        String bucket = "test";
        if (!minioService.isBucketExisted(bucket)) {
            minioService.createBucket(bucket);
        }
        String objectName = "test.txt";
        String content = "Hello, MinIO!";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucket).object(objectName).stream(
                                inputStream, inputStream.available(), -1)
                        .contentType("text/plain")
                        .build()
        );
    }
}
