package com.hbsd.rjxy.miaomiao.zsh.self.control;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.zsh.self.service.SelfService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/Self")
public class SelfControl {
    @Autowired
    private SelfService selfService;

    /**
     * @Param request
     * @Param response
     */
    @RequestMapping("/edit")
    @ResponseBody
    public void updateUser(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        InputStream is= null;
        String param=null;
        try {
            is = request.getInputStream();
            byte[]buffer=new byte[1024];
            int len=is.read(buffer);
            param=new String(buffer,0,len);
            JSONObject object=new JSONObject(param);
            JSONObject res=new JSONObject();
            String username=object.getString("newName");
            String sex=object.getString("newSex");
            String uintro=object.getString("newIntro");
            Integer uid=object.getInt("uid");
            int rtn=selfService.updateUserMsgById(username,sex,uintro,uid);
            if (rtn>0){
                res.put("edited","ok");
            }else{
                res.put("edited","error");
            }
            response.getWriter().append(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Param request
     * @Param response
     */
    @RequestMapping("/find")
    @ResponseBody
    public void findUser(HttpServletRequest request, HttpServletResponse response){

        response.setCharacterEncoding("UTF-8");
        InputStream is= null;
        String param=null;
        try {
            is = request.getInputStream();
            byte[]buffer=new byte[255];
            int len=is.read(buffer);
            param=new String(buffer,0,len);
            JSONObject object=new JSONObject(param);
            JSONObject res=new JSONObject();
            if(object.length()!=0){
                int id=object.getInt("uid");
                System.out.println(id+"");

                User user=selfService.findUserById(id);
                /*发送给用户的信息*/
                System.out.print(user.getUid());
                System.out.print(user.getUsername());
                System.out.print(user.getUintro());
                System.out.print(user.getUsex());

                res.put("uid",user.getUid());
                res.put("uName",user.getUsername());
                res.put("uSex",user.getUsex());
                res.put("uIntro",user.getUintro());

            }
            is.close();
            response.getWriter().append(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
