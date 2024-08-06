package com.demo.minio;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private MinioClient minioClient;

    public boolean isBucketExisted(String bucket) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(bucket)
                            .build());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createBucket(String bucket) {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucket)
                            .build());
        } catch (Exception e) {
            log.info("Can't create bucket");
            return false;
        }
        return true;
    }

    public void uploadFile(String bucket, String objectName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .contentType(file.getContentType())
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            log.info("Can't upload file");
        }
    }

    public void deleteFile(String bucket, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.info("Can't remove file");
        }
    }

    public InputStream downloadFile(String bucket, String objcetName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .object(objcetName)
                            .bucket(bucket)
                            .build());
        } catch (Exception e) {
            log.info("Can't download file");
        }
        return null;
    }

    public List<String> getListFileName(String bucket, String path) {
        List<Item> items = new ArrayList<>();
        try {
            Iterable<Result<Item>> resultIterator = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(path)
                            .recursive(true)
                            .build());
            for (Result<Item> result : resultIterator) {
                items.add(result.get());
            }

        } catch (Exception e) {
            log.info("Can't create bucket");
        }
        return items.stream().map(Item::objectName).toList();
    }

    public void putListObject(String bucket, List<MultipartFile> multipartFiles) {
        List<SnowballObject> objectList = toListSnowBall(multipartFiles);

        try {
            minioClient.uploadSnowballObjects(
                    UploadSnowballObjectsArgs.builder()
                            .bucket(bucket)
                            .objects(objectList)
                            .build());
        } catch (Exception e) {
            log.info("Can't upload list file");
        }
    }

    public List<SnowballObject> toListSnowBall(List<MultipartFile> multipartFiles) {
        List<SnowballObject> listFile = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()) {
                listFile.add(new SnowballObject(file.getName(), inputStream, inputStream.available(), ZonedDateTime.now()));
            } catch (Exception e) {
                log.info("Can't create file snowball", file.getName());
            }
        }
        return listFile;
    }
}
