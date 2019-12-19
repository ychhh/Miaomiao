package com.hbsd.rjxy.miaomiao.zsh.self.control;

import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.ljt.Login.util.DecodeUtil;
import com.hbsd.rjxy.miaomiao.zlc.utils.RequestUtil;
import com.hbsd.rjxy.miaomiao.zlc.video.service.VideoService;
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
    @Autowired
    private VideoService videoService;

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
            System.out.println("用户"+uid);
            if(object.getBoolean("isEditedHead")){
                String hpath=object.getString("newHpath");
                int rtn2=selfService.updateUserHpathById(hpath,uid);
                System.out.println("============="+hpath);
                System.out.println("rtn2"+rtn2);
            }
            int rtn=selfService.updateUserMsgById(username,sex,uintro,uid);

            res.put("edited","ok");
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
    @RequestMapping("/editPwd")
    @ResponseBody
    public void editUserPwd(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        InputStream is= null;
        String param=null;
        try {
            is = request.getInputStream();
            byte[]buffer=new byte[1024];
            int len=is.read(buffer);
            param=new String(buffer,0,len);
            JSONObject object=new JSONObject(param);

            String newPwd= DecodeUtil.decodeToString(object.getString("newPwd"));

            Integer uid=object.getInt("uid");

            int e=selfService.updatePwdById(newPwd,uid);

            response.getWriter().append(e+"");
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
    @RequestMapping("/editWithOldPwd")
    @ResponseBody
    public void editPwdWithOldPwd(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        System.out.println("来修改密码啦");
        InputStream is= null;
        String param=null;
        try {
            is = request.getInputStream();
            byte[]buffer=new byte[1024];
            int len=is.read(buffer);
            param=new String(buffer,0,len);
            JSONObject object=new JSONObject(param);

            String newPwd= DecodeUtil.decodeToString(object.getString("newPwd"));
            String oldPwd=DecodeUtil.decodeToString(object.getString("oldPwd"));
            Integer uid=object.getInt("uid");
            if(selfService.confirmPwd(uid,oldPwd)){
                int e=selfService.updatePwdById(newPwd,uid);
                response.getWriter().append(e+"");
            }
            else{
                System.out.println("旧密码输入错误");
                response.getWriter().append("false");
            }

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
                if(user!=null){
                    System.out.print(user.getUid());
                    System.out.print(user.getUsername());
                    System.out.print(user.getUintro());
                    System.out.print(user.getUsex());

                    res.put("uid",user.getUid());
                    res.put("uName",user.getUsername());
                    res.put("uSex",user.getUsex());
                    res.put("uIntro",user.getUintro());
                    res.put("hpath",user.getHpath());
                }
            }
            is.close();
            response.getWriter().append(res.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getToken")
    @ResponseBody
    public String getToken(HttpServletRequest request, HttpServletResponse response){
        try {
            //
            JSONObject jsonObject = new JSONObject(RequestUtil.getJson(request));
            System.out.println(jsonObject.get("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoService.getToken();
    }



}
