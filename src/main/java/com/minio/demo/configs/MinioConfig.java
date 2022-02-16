package com.minio.demo.configs;

/**
 * @ClassName: MinioConfig
 * @Description: Minio配置类
 * @author: Gatsby
 * @date: 2022/2/10 17:30
 */

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
@EnableConfigurationProperties(MinioPropertiesConfig.class)
public class MinioConfig {

    @Resource
    private MinioPropertiesConfig minioPropertiesConfig;


    /**
     * @MethodName:  minioClient
     * @Parameter: []
     * @Return io.minio.MinioClient
     * @Description: 初始化MinIO客户端
     * @author: Gatsby
     * @date:  2022/2/16 14:21
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioPropertiesConfig.getEndpoint())
                .credentials(minioPropertiesConfig.getAccessKey(), minioPropertiesConfig.getSecretKey())
                .build();
    }
}




