package com.hbsd.rjxy.miaomiao.entity;

import java.util.Comparator;

public class SortClass implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Comment c1 = (Comment)o1;
        Comment c2 = (Comment)o2;

        int flag = c2.getCreateTime().compareTo(c1.getCreateTime());
        return flag;
    }
}
