package com.hbsd.rjxy.miaomiao.zsh.self.service;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.Login.Constant;
import com.hbsd.rjxy.miaomiao.zsh.self.dao.SelfDao;
import org.apache.commons.codec.digest.DigestUtils;
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
    @Transactional
    public Boolean confirmPwd(Integer uid,String oldPwd) {
        String data_pwd = selfDao.findUserByUid(uid).getPwd();
        String old_md5Pwd = DigestUtils.md5Hex(DigestUtils.md5Hex(oldPwd) + Constant.SALT);
        System.out.println("数据库的旧密码是"+oldPwd);
        System.out.println("加密后的旧密码是"+old_md5Pwd);

        if (data_pwd.equals(old_md5Pwd)) {
            return true;
        }
        return false;
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

    /**
     * 根据uid更改用户信息（姓名、性别、简介）
     * @param username
     * @param sex
     * @param uintro
     * @param uid
     * @param hpath
     * @return
     */
    @Transactional
    public int updateUserMsgById(String username,String sex,String uintro,Integer uid,String hpath){
        return selfDao.updateUserMsgById(username,sex,uintro,uid,hpath);
    }

    /**
     *
     * @param pwd
     * @param id
     * @return
     */
    @Transactional
    public int updatePwdById(String pwd,Integer id){
       String md5Pwd= DigestUtils.md5Hex(DigestUtils.md5Hex(pwd)+ Constant.SALT);
        return selfDao.updateUserPwdById(md5Pwd,id);
    }


}
