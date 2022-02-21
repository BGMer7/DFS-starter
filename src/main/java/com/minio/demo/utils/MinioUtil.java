package com.minio.demo.utils;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: MinioUtil
 * @Description: MinIO工具类
 * @author: Gatsby
 * @date: 2022/2/10 17:31
 */
@Component
public class MinioUtil {
    @Resource
    private MinioClient minioClient;

    /**
     * @MethodName: existBucket
     * @Parameter: [bucketName]
     * @Return boolean
     * @Description: 判断是否存在这个桶
     * @author: Gatsby
     * @date: 2022/2/16 10:50
     */
    public boolean existBucket(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            // TODO throw...
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @MethodName: makeBucket
     * @Parameter: [bucketName]
     * @Return boolean
     * @Description: 创建一个桶，
     * 如果已经存在，返回false；
     * 如果创建失败，返回false，打印异常；
     * 如果创建成功，返回true。
     * @author: Gatsby
     * @date: 2022/2/16 10:53
     */
    public boolean makeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (!exist) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                return true;
            } catch (Exception e) {
                // TODO throw...
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @MethodName: getBucketNameList
     * @Parameter: []
     * @Return java.util.List<java.lang.String>
     * @Description: 获取所有的桶的名称
     * @author: Gatsby
     * @date: 2022/2/16 11:05
     */
    public List<String> getBucketNameList() {
        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            List<String> bucketNameList = new ArrayList<>();
            for (Bucket bucket : bucketList) {
                bucketNameList.add(bucket.name());
            }
            return bucketNameList;
        } catch (Exception e) {
            // TODO throw...
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @MethodName: getBucketList
     * @Parameter: []
     * @Return java.util.List<io.minio.messages.Bucket>
     * @Description: 获取所有的桶
     * @author: Gatsby
     * @date: 2022/2/16 11:05
     */
    public List<Bucket> getBucketList() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            // TODO throw...
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @MethodName: removeBucket
     * @Parameter: [bucketName]
     * @Return boolean
     * @Description: 删除桶，只有桶为空的时候才能删除
     * 删除之后再次通过bucketExist检查是否还存在，确实不存在之后再返回true
     * @author: Gatsby
     * @date: 2022/2/16 13:55
     */
    public boolean removeBucket(String bucketName) {
        boolean exist = existBucket(bucketName);
        if (exist) {
            try {
                Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    // 有对象文件，则删除失败
                    if (item.size() > 0) {
                        return false;
                    }
                }
                // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                // 重新检查是否已经不存在，如果不存在了就返回true
                exist = existBucket(bucketName);
                if (!exist) {
                    return true;
                }
            } catch (Exception e) {
                // TODO throw...
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // TODO 涉及到在文件服务器中的文件命名格式
    // 沿用WebRTC生成的文件名还是重新命名，这个url需要保存用以查询双录文件

    /**
     * @MethodName: uploadObject
     * @Parameter: [bucketName, objectName, filePath]
     * @Return boolean
     * @Description: Uploads contents from a file as object in bucket.
     * 将一个文件上传到MinIO作为一个对象保存
     * @author: Gatsby
     * @date: 2022/2/16 14:08
     */
    public boolean uploadObject(String bucketName, String objectName, String filePath) {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .contentType("video/mp4")
                            .build());
            return true;
        } catch (Exception e) {
            // TODO throw...
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @MethodName: putObject
     * @Parameter: [bucketName, multipartFile]
     * @Return java.util.List<java.lang.String>
     * @Description:
     * @author: Gatsby
     * @date: 2022/2/21 18:52
     */
    public boolean putObject(String bucketName, String ObjectName, MultipartFile multipartFile) {
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(ObjectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(multipartFile.getContentType())
                    .build());
            return true;
        } catch (Exception e) {
            // TODO throws...
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO throws...
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @MethodName: putImageByBase64
     * @Parameter: [bucketName, ObjectName, Base64]
     * @Return boolean
     * @Description: 通过发送Base64，转换成InputStream再上传MinIO文件服务器
     * @author: Gatsby
     * @date: 2022/2/21 19:14
     */
    public boolean putImageByBase64(String bucketName, String ObjectName, String base64) {
        InputStream inputStream = Base64ToInputStreamUtil.base64ToInputStream(base64);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(ObjectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType("image/jpeg")
                    .build());
            return true;
        } catch (Exception e) {
            // TODO throws...
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO throws...
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @MethodName: downloadObject
     * @Parameter: [bucketName, objectName, outputName]
     * @Return boolean
     * @Description: Download object given the bucket, object name and output file name
     * 给定bucket、object名、输出名，将对象下载下来并以文件形式保存
     * @author: Gatsby
     * @date: 2022/2/16 15:24
     */
    public boolean downloadObject(String bucketName, String objectName) {
        try {
            String outputPath = "src/main/resources/output/" + objectName;
            System.out.println(outputPath);
            File file = new File(outputPath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File " + objectName + " has existed.");
            }
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(outputPath)
                            .build());
            return true;
        } catch (Exception e) {
            // TODO throw...
            e.printStackTrace();
            return false;
        }
    }
}
