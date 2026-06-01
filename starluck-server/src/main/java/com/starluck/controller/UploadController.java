package com.starluck.controller;

import cn.hutool.core.util.IdUtil;
import com.starluck.common.Result;
import com.starluck.common.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author AI
 * @date 2026-06-01
 */
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${app.upload-path:./uploads}")
    private String uploadPath;

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = SecurityUtil.getCurrentUserId();
        String url = saveFile(file, "avatar", userId);
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.ok(result);
    }

    /**
     * 上传图片
     */
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = SecurityUtil.getCurrentUserId();
        String url = saveFile(file, "image", userId);
        Map<String, String> result = new HashMap<>();
        result.put("url", url);
        return Result.ok(result);
    }

    private String saveFile(MultipartFile file, String type, Long userId) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = type + "_" + userId + "_" + IdUtil.fastSimpleUUID().substring(0, 8) + ext;

        Path dir = Paths.get(uploadPath, type);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        Path filePath = dir.resolve(fileName);
        file.transferTo(filePath.toFile());

        return "/uploads/" + type + "/" + fileName;
    }
}
