package com.hbsd.rjxy.miaomiao.ych.subscription.dao;

import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.GeneratedValue;
import java.util.List;

public interface SubDao extends JpaRepository<Subscription_record,Integer>{
    @Query( value = "SELECT * FROM subscription_record WHERE uid=?",nativeQuery = true)
    List<Subscription_record> findAllByUid(int uid);
    @Query( value = "SELECT * FROM subscription_record WHERE cid=?",nativeQuery = true)
    List<Subscription_record> findAllByCid(int cid);
    @Query( value = "SELECT * FROM subscription_record WHERE uid=? and cid=?",nativeQuery = true)
    List<Subscription_record> findAllByUidAndUid(int uid,int cid);
    @Modifying
    @Transactional(readOnly = false)
    @Query( value = "UPDATE subscription_record set subscription_status=0 where uid=? and cid=?",nativeQuery = true)
    int unfollow(int uid,int cid);
    @Modifying
    @Transactional(readOnly = false)
    @Query( value = "UPDATE subscription_record set subscription_status=1 where uid=? and cid=?",nativeQuery = true)
    int follow(int uid,int cid);
}
