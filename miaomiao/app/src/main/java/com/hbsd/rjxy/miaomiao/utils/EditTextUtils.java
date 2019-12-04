package com.hbsd.rjxy.miaomiao.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hbsd.rjxy.miaomiao.R;

public class EditTextUtils {
    private static boolean isHideFirst = true;// 输入框密码是否是隐藏的，默认为true

    /**
     * 对文本编辑框一键清除的控制
     * @param et 文本编辑框
     * @param view 控制删除的控件
     */

    public static void clearButtonListener(final EditText et, final View view) {
        // 取得et中的文字
        String etInputString = et.getText().toString();
        // 根据et中是否有文字进行X可见或不可见的判断
        if (TextUtils.isEmpty(etInputString)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        //点击X时使et中的内容为空
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                et.requestFocusFromTouch();
            }
        });
        //对et的输入状态进行监听
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 对密码部分密码显示与否，一键删除的操作
     * @param et 密码编辑框
     * @param viewClear 控制删除的控件
     * @param viewEye  控制是否显示明文密码的控件
     */
    public static void clearAndShowButtonListener(final EditText et, final View viewClear,final ImageView viewEye) {
        // 取得et中的文字
        String etInputString = et.getText().toString();
        // 根据et中是否有文字进行X可见或不可见的判断
        if (TextUtils.isEmpty(etInputString)) {
            viewClear.setVisibility(View.INVISIBLE);
            viewEye.setVisibility(View.INVISIBLE);
        } else {
            viewClear.setVisibility(View.VISIBLE);
            viewEye.setVisibility(View.VISIBLE);
        }
        //点击X时使et中的内容为空
        viewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                et.requestFocusFromTouch();
            }
        });
        //对et的输入状态进行监听
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    viewClear.setVisibility(View.INVISIBLE);
                    viewEye.setVisibility(View.INVISIBLE);
                } else {
                    viewClear.setVisibility(View.VISIBLE);
                    viewEye.setVisibility(View.VISIBLE);
                }
            }
        });

        viewEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对显示明文密文的操作
                if (isHideFirst == true) {
                    viewEye.setImageResource(R.drawable.eye_open);
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    et.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    viewEye.setImageResource(R.drawable.eye_close);
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    et.setTransformationMethod(method);
                    isHideFirst = true;
                }
                // 光标的位置
                int index = et.getText().toString().length();
                et.setSelection(index);
            }
        });
    }
}
