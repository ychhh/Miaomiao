package com.hbsd.rjxy.miaomiao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MeBufferReader {
    /**
     * 读取返回结果
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = is.read(buffer)) != -1){
            bos.write(buffer,0,len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
