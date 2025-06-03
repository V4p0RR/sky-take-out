package com.sky.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
  @Autowired
  private AliOssUtil aliOssUtil;

  public String upload(MultipartFile file) {
    try {
      log.info("文件上传");
      String originalFilename = file.getOriginalFilename();
      String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
      String objectName = UUID.randomUUID().toString() + substring; // 搞到文件名objectName
      String img = aliOssUtil.upload(file.getBytes(), objectName);
      log.info("上传成功:{}", img);
      return img;
    } catch (IOException e) {
      log.info("文件上传失败:{}", e);
      return null;
    }

  }
}
