package com.demo.minio;

public enum MinioError {
    ME01("Can't create bucket"),
    ME02("Can't upload file"),
    ME03("Can't remove file"),
    ME04("Can't download file"),
    ME05("Can't get list file name"),
    ME06("Can't upload list file"),
    ME07("Can't create file snowball {}");


    MinioError(String message) {
    }
}
