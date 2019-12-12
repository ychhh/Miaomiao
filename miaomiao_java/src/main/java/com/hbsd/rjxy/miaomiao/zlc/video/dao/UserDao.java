package com.hbsd.rjxy.miaomiao.zlc.video.dao;

import com.hbsd.rjxy.miaomiao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User,Integer> {



    @Query
    User findUserByUid(int uid);




}
