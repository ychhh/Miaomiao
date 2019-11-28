package com.hbsd.rjxy.miaomiao.ych.subscription.service;


import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import com.hbsd.rjxy.miaomiao.ych.subscription.dao.SubDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubService {
    @Autowired
    private SubDao subDao;

    public List<Subscription_record> findall(){
        return subDao.findAll();
    }
    public List<Subscription_record> findOneByUid(int uid){
        return subDao.findAllByUid(uid);
    }
}
