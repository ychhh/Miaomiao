package com.hbsd.rjxy.miaomiao.ych.cat.dao;

import com.hbsd.rjxy.miaomiao.entity.Cat;
import com.hbsd.rjxy.miaomiao.entity.Subscription_record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CatDao extends JpaRepository<Cat,Integer>, JpaSpecificationExecutor<Cat> {
    @Query( value = "SELECT * FROM cat WHERE uid=?",nativeQuery = true)
    List<Cat> findAllByUid(int uid);
    @Query( value = "SELECT * FROM cat WHERE cid=?",nativeQuery = true)
    Cat findAllByCid(int cid);
}
