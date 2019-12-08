package com.hbsd.rjxy.miaomiao.ljt.Login.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DecodeUtil {
    public static String decodeToString(String encodedText){
        String text=null;
        try {
            text=new String(Base64.getDecoder().decode(encodedText), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
