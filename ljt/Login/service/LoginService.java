package com.hbsd.rjxy.miaomiao.ljt.Login.service;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.Login.Constant;
import com.hbsd.rjxy.miaomiao.ljt.Login.dao.LoginDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class LoginService {
    @Autowired
    private LoginDao loginDao;

    /**
     * 增加用户
     * @param tel
     */
    @Transactional
    public void saveUser(String tel, Date uregist){
        User user=new User();
        user.setTel(DigestUtils.md5Hex(DigestUtils.md5Hex(tel)+ Constant.SALT));
        user.setUregist(uregist);
        loginDao.save(user);
    }

    /**
     * 根据手机号查询user
     * @param tel
     */
    @Transactional
    public User findUserByTel(String tel){
        User user=loginDao.findUserByTel(DigestUtils.md5Hex(DigestUtils.md5Hex(tel)+ Constant.SALT));
        return user;
    }

    /**
     *
     */
    @Transactional
    public void updateUserUlast_LoginById(Date lastLoginTime,Integer uid){
        loginDao.updateUserUlast_LoginById(lastLoginTime,uid);
    }
}
