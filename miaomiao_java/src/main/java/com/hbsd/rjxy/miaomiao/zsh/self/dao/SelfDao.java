package com.hbsd.rjxy.miaomiao.zsh.self.dao;

import com.hbsd.rjxy.miaomiao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface SelfDao extends JpaRepository<User,Integer> {
    /**
     * 根据手机号查询User
     * @param tel
     * @return
     */
    @Query
    User findUserByTel(String tel);

    /**
     * 根据uid查询user
     */
    @Query
    User findUserByUid(Integer uid);

    /**
     * 根据id修改上一次登陆时间
     * @param time
     * @param uid
     */
    @Modifying
    @Query(value = "update user set ulast_login=:time where uid=:uid",nativeQuery = true)
    void updateUserUlast_LoginById(@Param("time") Date time, @Param("uid")Integer uid);
    /**
     * 根据id修改姓名
     * @param name
     * @param uid
     */
    @Query(value = "update user set username=:name where uid =:uid",nativeQuery = true)
    void updateUserNameById(@Param("name") String name,@Param("uid")Integer uid);
    /**
     * 根据id修改性别
     * @Param sex;
     * @Param uid
     */
    @Query(value = "update user set usex=:sex where uid =:uid",nativeQuery = true)
    void updateUserSexById(@Param("sex")String sex,@Param("uid") Integer uid);

}
