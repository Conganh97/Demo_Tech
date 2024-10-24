package com.demo.minio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/minio")
@Tag(name = "Minio Controller", description = "Controller for Minio")
public class MinioController {
    private static MinioService minioService;

    @Operation(summary = "Say Hello", description = "Returns a simple greeting message")
    @PostMapping("/{bucket}/upload")
    public ResponseEntity<Boolean> uploadFile(@PathVariable String bucket, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(minioService.uploadFile("test", file.getOriginalFilename(), file));
    }

    @DeleteMapping("/{bucket}/{path}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable String bucket, @PathVariable String path) {
        return ResponseEntity.ok().body(minioService.deleteFile(bucket, path));
    }

    @GetMapping("/{bucket}/{path}")
    public ResponseEntity<InputStream> downloadFile(@PathVariable String bucket, @PathVariable String path) {
        return ResponseEntity.ok().body(minioService.downloadFile(bucket, path));
    }

    @GetMapping("/{bucket}/{path}/list")
    public ResponseEntity<List<String>> getListFileInBucket(@PathVariable String bucket, @PathVariable String path) {
        return ResponseEntity.ok().body(minioService.getListFileName(bucket, path));
    }

    @Operation(summary = "Say Hello", description = "Returns a simple greeting message")
    @PostMapping("/{bucket}/uploadList")
    public ResponseEntity<Boolean> uploadFile(@PathVariable String bucket, @RequestParam("file") List<MultipartFile> file) {
        return ResponseEntity.ok().body(minioService.uploadListObject(bucket, file));
    }

    @GetMapping("/{bucket}/{path}/share")
    public ResponseEntity<String> shareFile(@PathVariable String bucket, @PathVariable String path, @RequestBody int expire) {
        return ResponseEntity.ok().body(minioService.shareLink(bucket, path, expire));
    }
}
