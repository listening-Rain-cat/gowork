package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.exception.UploadFailedException;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 包名称： com.sky.controller.admin
 * 类名称：CommonController
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-15 18:06
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api("公用接口")
public class CommonController {

    @Value("${sky.upload.local-path:uploads}")
    private String uploadPath;

    @Value("${sky.upload.url-prefix:/upload/}")
    private String uploadUrlPrefix;

    @ApiOperation("文件上传接口")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        log.info("文件上传：{}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new UploadFailedException(MessageConstant.UPLOAD_FAILED);
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.toLowerCase().startsWith("image/")) {
            throw new UploadFailedException("只能上传图片文件");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (StringUtils.hasText(originalFilename) && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        try {
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);

            Path targetPath = uploadDir.resolve(fileName).normalize();
            if (!targetPath.startsWith(uploadDir)) {
                throw new UploadFailedException(MessageConstant.UPLOAD_FAILED);
            }

            file.transferTo(targetPath.toFile());
            return Result.success(normalizeUrlPrefix(uploadUrlPrefix) + fileName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new UploadFailedException(MessageConstant.UPLOAD_FAILED);
        }
    }

    private String normalizeUrlPrefix(String prefix) {
        if (!StringUtils.hasText(prefix)) {
            return "/upload/";
        }
        String normalized = prefix.startsWith("/") ? prefix : "/" + prefix;
        return normalized.endsWith("/") ? normalized : normalized + "/";
    }
}
