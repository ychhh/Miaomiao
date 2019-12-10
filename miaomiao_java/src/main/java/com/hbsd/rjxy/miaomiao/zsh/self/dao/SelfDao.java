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
    @Modifying
    @Query(value = "update user set username=:name where uid=:uid",nativeQuery = true)
    void updateUserNameById(@Param("name") String name,@Param("uid")Integer uid);
    /**
     * 根据id修改性别
     * @Param sex;
     * @Param uid
     */
    @Modifying
    @Query(value = "update user set usex=:sex where uid =:uid",nativeQuery = true)
    void updateUserSexById(@Param("sex")String sex,@Param("uid") Integer uid);
    /**
     * 根据id修改简介
     * @Param intro;
     * @Param uid
     */
    @Modifying
    @Query(value = "update user set uintro=:intro where uid =:uid",nativeQuery = true)
    void updateUserIntroById(@Param("intro")String intro,@Param("uid") Integer uid);
    /**
     * 根据id修改密码
     * @Param pwd;
     * @Param uid
     */
    @Modifying
    @Query(value = "update user set pwd=:pwd where uid =:uid",nativeQuery = true)
    int updateUserPwdById(@Param("pwd")String pwd,@Param("uid") Integer uid);

    @Modifying
    @Query(value = "update user set username=:name,uintro=:uintro,usex=:usex where uid =:uid",nativeQuery = true)
    int updateUserMsgById(@Param("name")String username,@Param("usex")String sex,@Param("uintro")String uintro,@Param("uid") Integer uid);
}
