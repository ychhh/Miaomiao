package com.hbsd.rjxy.miaomiao.zlc.utils;

public class FileUtil {

    // 图片允许的后缀扩展名
    public static String[] IMAGE_FILE_EXTD = new String[] { "png", "bmp", "jpg", "jpeg","pdf" };

    // 图片允许的后缀扩展名
    public static String[] VIDEO_FILE_EXTD = new String[] { "mp4" };

    public static boolean isImageAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isVideoAllowed(String fileName){
        for (String ext : VIDEO_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}

