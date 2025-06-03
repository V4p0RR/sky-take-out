package com.sky.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AliOssConfiguration { // 创建alioss工具类的对象 因为扫不到aliossproperties，故要注入第三方bean

  @Bean // 声明一个bean对象 方法返回值就是bean对象
  @ConditionalOnMissingBean
  AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) { // 创建一个工具类对象
    log.info("开始创建AliOssUtils对象:{}", aliOssProperties);
    return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(),
        aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());
  }
}
