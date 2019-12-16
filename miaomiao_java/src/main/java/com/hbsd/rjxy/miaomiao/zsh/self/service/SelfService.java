package com.hbsd.rjxy.miaomiao.zsh.self.service;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.self.dao.SelfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelfService {
    @Autowired
    private SelfDao selfDao;
    /**
     * 根据uid查询用户
     * @Param uid
     * */
    @Transactional
    public User findUserById(Integer uid){
        User user=selfDao.findUserByUid(uid);
        return user;
    }
    /**
     * 根据uid修改用户姓名
     * @Param name
     * @Param uid
     */
    @Transactional
    public void updateUserNameById(String name,Integer uid){
        selfDao.updateUserNameById(name,uid);
    }
    /**
     * 根据uid修改用户性别
     * @Param sex
     * @Param uid
     * */
    @Transactional
    public void updateUserSexById(String sex,Integer uid){
        selfDao.updateUserSexById(sex,uid);
    }
    /**
     * 根据uid修改简介
     * @Param intro
     * @Param uid
     * */
    @Transactional
    public void updateUserIntroById(String intro,Integer uid){
        selfDao.updateUserIntroById(intro,uid);
    }
    /**
     * 根据uid改变头像
     *
     */
    @Transactional
    public int updateUserHpathById(String hpath,Integer uid){
       return selfDao.updateUserHpathById("http://q20jftoug.bkt.clouddn.com/"+hpath,uid);
    }
    /**
     * 根据uid更改用户信息（姓名、性别、简介）
     * @param username
     * @param sex
     * @param uintro
     * @param uid
     * @return
     */
    @Transactional
    public int updateUserMsgById(String username,String sex,String uintro,Integer uid){
        return selfDao.updateUserMsgById(username,sex,uintro,uid);
    }


    /**
     * 根据id修改用户密码
     * @param pwd
     * @param id
     * @return
     */
    @Transactional
    public int updatePwdById(String pwd,Integer id){
        return selfDao.updateUserPwdById(pwd,id);
    }

}
