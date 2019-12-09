package com.hbsd.rjxy.miaomiao.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class EncodeUtil {
    public static String encodeToString(String text){
        String encodedText=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                encodedText= Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodedText;
    }
}
