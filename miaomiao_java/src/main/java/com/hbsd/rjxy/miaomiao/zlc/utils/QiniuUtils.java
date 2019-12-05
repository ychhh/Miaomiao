package com.hbsd.rjxy.miaomiao.zlc.utils;



import com.alibaba.fastjson.JSONObject;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class QiniuUtils {


    // 设置好账号的ACCESS_KEY和SECRET_KEY
    String accessKey =
            "ITGiHJmwEZeBfn6HNC76VWk5PahCoK7J9q7W36Qv";
    String secretKey =
            "pWk_nnowhVKlz3YVTGH4XAVcjtXprOYHQdFnts6_";
    String bucket =
            "hizt";
    String domain =
            "http://q20jftoug.bkt.clouddn.com";


    // 密钥配置
    Auth auth = Auth.create(accessKey, secretKey);
    // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
    Configuration cfg = new Configuration(Zone.zone0()
    );
    // ...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucket);
    }


    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getName().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getName().substring(dotPos + 1).toLowerCase();
            // 判断是否是合法的文件后缀
            if (!FileUtil.isImageAllowed(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回这张存储照片的地址
                return domain + "/" +JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                System.out.println("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            System.out.println("七牛异常:" + e.getMessage());
            return null;
        }
    }









}
