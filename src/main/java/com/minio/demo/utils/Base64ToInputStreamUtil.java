package com.minio.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * @ClassName: Base64ToInputStreamUtil
 * @Description: Base64转InputStream工具类
 * @author: Gatsby
 * @date: 2022/2/21 18:49
 */

public class Base64ToInputStreamUtil {
    /**
     * @MethodName: base64ToInputStream
     * @Parameter: [base64]
     * @Return java.io.InputStream
     * @Description: Base64字符串转InputStream
     * @author: Gatsby
     * @date: 2022/2/21 18:50
     */
    public static InputStream base64ToInputStream(String base64) {
        ByteArrayInputStream stream = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            stream = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }
}


