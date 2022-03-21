package com.minio.demo.controller;

import com.minio.demo.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * @ClassName: MinioController
 * @Description: MinIO控制类
 * @author: Gatsby
 * @date: 2022/2/16 16:20
 */

@Slf4j
@RestController
@RequestMapping("/minio")
public class MinioController {
    @Resource
    private MinioUtil minioUtil;

    /**
     * @MethodName: getBucketNameList
     * @Parameter: []
     * @Return java.util.List<java.lang.String>
     * @Description: 获取所有的bucket，返回一个列表
     * @author: Gatsby
     * @date: 2022/2/16 16:31
     */
    // TODO 返回值改成http响应
    @GetMapping("/getBucketNameList")
    public List<String> getBucketNameList() {
        List<String> bucketNameList = minioUtil.getBucketNameList();
        System.out.println(bucketNameList.toString());
        return bucketNameList;
    }

    /**
     * @MethodName: uploadObject
     * @Parameter: [bucketName, objectName, filePath]
     * @Return boolean
     * @Description: 上传文件
     * @author: Gatsby
     * @date: 2022/2/16 16:57
     */
    // TODO 返回值改成http响应
    @PostMapping("/uploadObject")
    public boolean uploadObject(@RequestParam("bucketName") String bucketName,
                                @RequestParam("objectName") String objectName,
                                @RequestParam("filePath") String filePath) {
        return minioUtil.uploadObject(bucketName, objectName, filePath);
    }


    // TODO 返回值改成http响应

    /**
     * @MethodName: putObject
     * @Parameter: [bucketName, objectName, file]
     * @Return boolean
     * @Description: 上传文件，直接通过file的形式上传
     * @author: Gatsby
     * @date: 2022/2/21 19:27
     */
    @PostMapping("/putObject")
    public boolean putObject(@RequestParam("bucketName") String bucketName,
                             @RequestParam("objectName") String objectName,
                             @RequestParam("file") MultipartFile file) {
        return minioUtil.putObject(bucketName, objectName, file);

    }

    // TODO 返回值改成http响应

    /**
     * @MethodName: putImageByBase64
     * @Parameter: [bucketName, objectName, base64]
     * @Return boolean
     * @Description: 上传图片文件，通过Base64上传
     * @author: Gatsby
     * @date: 2022/2/21 19:28
     */
    @PostMapping("/putImage")
    public boolean putImageByBase64(@RequestParam("bucketName") String bucketName,
                                    @RequestParam("objectName") String objectName,
                                    @RequestParam("base64") String base64) {
        return minioUtil.putImageByBase64(bucketName, objectName, base64);
    }

    /**
     * @MethodName: downloadObject
     * @Parameter: [bucketName, objectName, outputName]
     * @Return boolean
     * @Description: 下载Object
     * @author: Gatsby
     * @date: 2022/2/16 17:50
     */
    // TODO 返回值改成http响应
    @GetMapping("downloadObject")
    public boolean downloadObject(@RequestParam("bucketName") String bucketName,
                                  @RequestParam("objectName") String objectName) {
        return minioUtil.downloadObject(bucketName, objectName);
    }
}


