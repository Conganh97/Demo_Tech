package com.demo.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        String data = "Line 1\nLine 2\nLine 3";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        inputStream.mark(1024); // Đánh dấu vị trí hiện tại (cần đảm bảo InputStream hỗ trợ mark)

        // Đọc toàn bộ dữ liệu
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }

        inputStream.reset(); // Quay lại vị trí đã mark

        log.info("Reading again:");
        reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
    }
}