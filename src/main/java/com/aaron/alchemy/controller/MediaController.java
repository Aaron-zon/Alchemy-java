package com.aaron.alchemy.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jfunc.common.utils.StrUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class MediaController {
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    /**
     * @description: 视频分段播放
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/getStreamVideo")
    public void getStreamVideo(HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        File file = new File("D:\\Media\\009.mp4"); // 改成你的目标文件
        long fileLength = file.length();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");OutputStream outputStream = response.getOutputStream();) {
            //获取从那个字节开始读取文件
            String rangeString = request.getHeader("Range");
            // 第一次连接从第0位开始读取
            long range = 0;
            // rangeString有值表示不是第一次连接
            if (StrUtil.isNotBlank(rangeString)) {
                // 从请求中读取要从哪个位置开始读取
                range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }
            //设置内容类型
            response.setHeader("Content-Type", "video/mp4");
            //返回码需要为206，代表只处理了部分请求，响应了部分数据
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            // 移动访问指针到指定位置
            randomAccessFile.seek(range);

            // 每次请求只返回1MB的视频流
            byte[] bytes = new byte[1024 * 1024];
            int len = randomAccessFile.read(bytes);
            //设置此次相应返回的数据长度
            response.setContentLength(len);
            //设置此次相应返回的数据范围
            response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
            // 将这1MB的视频流响应给客户端
            outputStream.write(bytes, 0, len);
            System.out.println("返回数据区间:【"+range+"-"+(range+len)+"】");
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }

    }

    /**
     * @description: 音频分段播放
     * @param request
     * @param response
     */
    @GetMapping("/getStreamAudio")
    public void getStreamAudio(HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        File file = new File("D:\\Media\\009.mp3"); // 改成你的目标文件
        try (OutputStream outputStream = response.getOutputStream();RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
            long fileLength = file.length();

            String rangeString = request.getHeader("Range");
            long range = 0;
            if (StrUtil.isNotBlank(rangeString)) {
                range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            }

            response.setHeader("Content-Type", "video/mp3");
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            randomAccessFile.seek(range);

            byte[] bytes = new byte[1024 * 1024];
            int len = randomAccessFile.read(bytes);

            response.setContentLength(len);
            response.setHeader("Content-Range", "bytes " + range + "-" + (fileLength - 1) + "/" + fileLength);

            outputStream.write(bytes, 0, len);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * @description: 一次性读取全部视频内容
     * @return
     */
    @GetMapping(value = "/getVideo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getVideo() {
        try {
            // 设置视频文件路径
            String videoFilePath = "D:\\Media\\008.mp4"; // 替换为你的视频文件路径
            Path videoPath = Paths.get(videoFilePath);

            // 获取视频长度
            long videoLength = Files.size(videoPath);

            // 创建视频资源并返回
            Resource videoResource = new FileSystemResource(videoPath.toFile());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoLength))
                    .body(videoResource);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @description: 一次性读取全部音频内容
     * @return
     */
    @GetMapping(value = "/getAudio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getAudio() {
        try {
            // 设置视频文件路径
            String videoFilePath = "D:\\Media\\009.mp3"; // 替换为你的视频文件路径
            Path videoPath = Paths.get(videoFilePath);

            // 获取视频长度
            long videoLength = Files.size(videoPath);

            // 创建视频资源并返回
            Resource videoResource = new FileSystemResource(videoPath.toFile());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoLength))
                    .body(videoResource);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }


}
