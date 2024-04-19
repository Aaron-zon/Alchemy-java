package com.aaron.alchemy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/aDownloadFile")
    public void aDownloadFile(HttpServletRequest request, HttpServletResponse response) {
        String path = "D:\\Media\\";
        String fileName = "009.mp3";
        try (InputStream inputStream = new FileInputStream(path + fileName);) {
            fileName = URLEncoder.encode(fileName, "UTF-8");

            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

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

    @GetMapping("/ajaxDownloadFile")
    public void ajaxDownloadFile() {

    }
}
