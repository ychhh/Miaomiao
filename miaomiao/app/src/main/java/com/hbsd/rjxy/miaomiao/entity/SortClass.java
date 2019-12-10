package com.hbsd.rjxy.miaomiao.entity;

import java.util.Comparator;

public class SortClass implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Comment c1 = (Comment)o1;
        Comment c2 = (Comment)o2;

        int flag = c2.getPublishTime().compareTo(c1.getPublishTime());
        return flag;
    }
}
