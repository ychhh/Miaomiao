package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

public class EditPwdPresenterCompl implements EditPwdPresenter {


    @Override
    public void editPwdWithoutOld(Integer id,String newPwd) {
        /*TODO
            直接将id,新的密码发送到服务器
        */

    }

    @Override
    public void editPwdWithOld(Integer id,String oldPwd, String newPwd) {
        /*TODO
            将id，旧密码，新密码发送到服务器
            先进行id查询，再进行旧密码匹配，最后修改为新密码
        */

    }
}
