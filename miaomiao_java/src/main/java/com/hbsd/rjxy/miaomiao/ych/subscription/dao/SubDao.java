package com.hbsd.rjxy.miaomiao.ych.subscription.dao;

import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.GeneratedValue;
import java.util.List;

public interface SubDao extends JpaRepository<Subscription_record,Integer>{
    @Query( value = "SELECT * FROM subscription_record WHERE uid=?",nativeQuery = true)
    List<Subscription_record> findAllByUid(int uid);
}
