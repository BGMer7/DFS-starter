package com.minio.demo.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MinioPripertiesConfig
 * @Description: MinIO 配置信息类，从application.yml文件中读取
 * @author: Gatsby
 * @date: 2022/2/16 14:19
 */

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioPropertiesConfig {
    /**
     * 端点
     */
    private String endpoint;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;
}


