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

//@Service
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

    public void createBucketIfNotExited(String bucket) {
        if (isBucketExisted(bucket)) {
            return;
        }
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucket)
                            .build());
        } catch (Exception e) {
            log.info(MinioError.ME01.name());
        }
    }

    public boolean uploadFile(String bucket, String objectName, MultipartFile file) {
        createBucketIfNotExited(bucket);
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .contentType(file.getContentType())
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        } catch (Exception e) {
            log.info(MinioError.ME02.name());
            return false;
        }

        return true;
    }

    public boolean deleteFile(String bucket, String objectName) {
        createBucketIfNotExited(bucket);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.info(MinioError.ME03.name());
            return false;
        }

        return true;
    }

    public InputStream downloadFile(String bucket, String objectName) {
        createBucketIfNotExited(bucket);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .object(objectName)
                            .bucket(bucket)
                            .build());
        } catch (Exception e) {
            log.info(MinioError.ME04.name());
        }
        return null;
    }

    public List<String> getListFileName(String bucket, String path) {
        createBucketIfNotExited(bucket);
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
            log.info(MinioError.ME05.name());
        }
        return items.stream().map(Item::objectName).toList();
    }

    public boolean uploadListObject(String bucket, List<MultipartFile> multipartFiles) {
        createBucketIfNotExited(bucket);
        List<SnowballObject> objectList = toListSnowBall(multipartFiles);

        try {
            minioClient.uploadSnowballObjects(
                    UploadSnowballObjectsArgs.builder()
                            .bucket(bucket)
                            .objects(objectList)
                            .build());
        } catch (Exception e) {
            log.info(MinioError.ME06.name());
            return false;
        }
        return true;
    }

    public String shareLink(String bucket, String path, int expiry) {
        try {
             return  minioClient.getPresignedObjectUrl(
                     GetPresignedObjectUrlArgs
                             .builder()
                             .bucket(bucket)
                             .object(path)
                             .expiry(expiry)
                             .build());
        } catch (Exception e) {
            log.info(MinioError.ME06.name());
            return null;
        }
    }

    private List<SnowballObject> toListSnowBall(List<MultipartFile> multipartFiles) {
        List<SnowballObject> listFile = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            try (InputStream inputStream = file.getInputStream()) {
                listFile.add(new SnowballObject(file.getName(), inputStream, inputStream.available(), ZonedDateTime.now()));
            } catch (Exception e) {
                log.info(MinioError.ME07.name(), file.getName());
            }
        }
        return listFile;
    }

    public static void main(String[] args) {
        System.out.println(UCLN(357,234));
    }

    public static int UCLN(int a, int b){
        if (b == 0)
            return a;
        int temp = a % b;
        return UCLN(b,temp);
    }
}