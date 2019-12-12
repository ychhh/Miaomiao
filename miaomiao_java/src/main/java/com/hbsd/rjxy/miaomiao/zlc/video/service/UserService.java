package com.hbsd.rjxy.miaomiao.zlc.video.service;


import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zlc.video.dao.UserDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;


    /**
     *  根据uid查询hpath和username
     * @param uid
     * @return
     */
    public String findHpathAndUsername(int uid){
        User user = userDao.findUserByUid(uid);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hpath",user.getHpath());
            jsonObject.put("username",user.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }




}
