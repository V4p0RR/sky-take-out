package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.result.Result;
import com.sky.service.CommonService;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用类控制器
 */
@RestController
@Slf4j
@RequestMapping("admin/common") // 定义基址url
public class CommonController {
  @Autowired
  private CommonService commonService;

  /**
   * 文件上传
   * 
   * @return
   */
  @PostMapping("/upload")
  public Result<String> upload(MultipartFile file) {
    log.info("文件上传");
    String img = commonService.upload(file);
    return Result.success(img);
  }

}
