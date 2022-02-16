package com.minio.demo.controller;

import com.minio.demo.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @MethodName: putObject
     * @Parameter: [bucketName, objectName, filePath]
     * @Return boolean
     * @Description: 上传文件
     * @author: Gatsby
     * @date: 2022/2/16 16:57
     */
    // TODO 返回值改成http响应
    @PostMapping("/putObject")
    public boolean putObject(@RequestParam("bucketName") String bucketName,
                             @RequestParam("objectName") String objectName,
                             @RequestParam("filePath") String filePath) {
        return minioUtil.putObject(bucketName, objectName, filePath);
    }

    /**
     * @MethodName:  downloadObject
     * @Parameter: [bucketName, objectName, outputName]
     * @Return boolean
     * @Description: 下载Object
     * @author: Gatsby
     * @date:  2022/2/16 17:50
     */
    @GetMapping("downloadObject")
    public boolean downloadObject(@RequestParam("bucketName") String bucketName,
                                  @RequestParam("objectName") String objectName,
                                  @RequestParam("outputName") String outputName) {
        return minioUtil.downloadObject(bucketName, objectName, outputName);
    }
}


