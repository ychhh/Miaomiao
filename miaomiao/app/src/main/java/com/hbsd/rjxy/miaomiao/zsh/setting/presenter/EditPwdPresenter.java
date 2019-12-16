package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

public interface EditPwdPresenter {
    void editPwdWithoutOld(Integer id,String newPwd);
    void editPwdWithOld(Integer id,String oldPwd,String newPwd);
}
