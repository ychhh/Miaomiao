package com.hbsd.rjxy.miaomiao.ych.subscription.service;


import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import com.hbsd.rjxy.miaomiao.ych.subscription.dao.SubDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SubService {
    @Autowired
    private SubDao subDao;
    public List<Subscription_record> findall(){
        return subDao.findAll();
    }
    public List<Subscription_record> findAllByUid(int uid){
        for (Subscription_record record:subDao.findAllByUid(uid)){
            System.out.println("1"+record.toString());
        }
        return subDao.findAllByUid(uid);
    }
    public List<Subscription_record> findAllByCid(int cid){
        return subDao.findAllByCid(cid);
    }
    public List<Subscription_record> findOneByUidAndCid(int uid,int cid){
        return subDao.findAllByUidAndUid(uid,cid);
    }
    public List<Subscription_record> follow(int uid, int cid){
        if(findOneByUidAndCid(uid, cid).size()!=0){
            subDao.follow(uid,cid);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return findAllByUid(uid);
        }else {
            Date date=new Date();
            Subscription_record subscription_record=new Subscription_record();
            subscription_record.setCid(cid);
            subscription_record.setUid(uid);
            subscription_record.setSubscription_status(1);
            Date sqlDate = new java.sql.Date(new Date().getTime());
            subscription_record.setSubscription_time((java.sql.Date) sqlDate);
            System.out.println(sqlDate);
            System.out.println(subscription_record.getSubscription_time());
            subDao.save(subscription_record);
            return findAllByUid(uid);
        }
    };
    public int unfollow(int uid,int cid){return subDao.unfollow(uid,cid);};
}
