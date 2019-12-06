package com.hbsd.rjxy.miaomiao.zsh.self.test;

import com.hbsd.rjxy.miaomiao.zsh.self.control.SelfControl;
import com.hbsd.rjxy.miaomiao.zsh.self.service.SelfService;

public class SelfTest {


    public static void main(String[] args) {
         SelfControl selfControl=new SelfControl();
         selfControl.testUpdate("zsh",1);
    }
}
