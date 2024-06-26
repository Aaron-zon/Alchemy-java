package com.aaron.alchemy.controller;

import com.aaron.alchemy.model.dto.FileChunkDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 文件下载
     * @param request
     * @param response
     */
    @GetMapping("/downloadFile")
    public void aDownloadFile(HttpServletRequest request, HttpServletResponse response) {
        String path = "D:\\Media\\";
        String fileName = "008.mp4";
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

    /**
     * 文件上传
     * @param request
     * @param response
     */
    @PostMapping("/uploadFile")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            MultipartFile file = multipartHttpServletRequest.getFile("file");

            String fileName =  ((new Date()).getTime()) + file.getOriginalFilename();

            String path = "D:\\Media\\" + fileName;
            File uploadFile = new File(path);
            file.transferTo(uploadFile);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 分片上传
     * @param chunk
     * @param fileChunk
     * @return
     */
    @PostMapping("/uploadChunk")
    public Boolean uploadChunk(@RequestParam("chunk") MultipartFile chunk, FileChunkDTO fileChunk) {
        String filePath = "D:\\Media\\";
        String fullPath = filePath + fileChunk.getFileName();
        try (RandomAccessFile rf = new RandomAccessFile(fullPath, "rw")) {
            rf.seek(fileChunk.getStart());
            rf.write(chunk.getBytes());
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return false;
        }

    }
}
