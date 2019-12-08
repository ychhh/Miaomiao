package com.hbsd.rjxy.miaomiao.zsh.self.test;
import com.hbsd.rjxy.miaomiao.zsh.self.service.SelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/self/test")
public class SelfTest {
    @Autowired
    private SelfService selfService;
    @RequestMapping("/update")
    @ResponseBody
    public void updateUser(){
        //Test test=new Test();
       // test.save();
        String name="zsh";
        int uid=4;

        selfService.updateUserNameById(name,uid);
    }





}
