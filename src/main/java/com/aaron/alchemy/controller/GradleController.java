package com.aaron.alchemy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Gradle 下载
 */
@RestController
public class GradleController {
    private static final Logger logger = LoggerFactory.getLogger(GradleController.class);


    @GetMapping("/gradleDownload")
    public void gradleDownload(HttpServletRequest request, HttpServletResponse response) {
        String path = "D:\\gradle\\";
        String fileName = "gradle-6.1.1-all.zip";
        File file = new File(path + fileName);
        try (InputStream inputStream = new FileInputStream(path + fileName);) {
            fileName = URLEncoder.encode(fileName, "UTF-8");

            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int length = inputStream.read(b);
            while (length != -1) {
                outputStream.write(b, 0, length);
                outputStream.flush();
                length = inputStream.read(b);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
