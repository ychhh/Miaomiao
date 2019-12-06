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

}
