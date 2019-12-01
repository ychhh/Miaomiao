package com.hbsd.rjxy.miaomiao.entity;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 *
 * 使用EventBus时可以使用这个类来传递数据
 *
 * MK代表map类型的key的类型，MV是值的类型
 * T代表List类型的item的类型
 *
 */
public class EventInfo<MK,MV,T> {

    private boolean available = true;   //默认是可用的（指包含有效信息）
    private Map<MK,MV> contentMap;
    private List<T> contentList;
    private String contentString;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Map<MK, MV> getContentMap() {
        return contentMap;
    }

    public void setContentMap(Map<MK, MV> contentMap) {
        this.contentMap = contentMap;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "contentMap=" + contentMap +
                ", contentList=" + contentList +
                ", contentString='" + contentString + '\'' +
                '}';
    }
}
