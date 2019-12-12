package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.entity.User;
import com.hbsd.rjxy.miaomiao.utils.Constant;
import com.hbsd.rjxy.miaomiao.zsh.setting.view.ShowCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class GetUserCardCompl implements GetUserCardPresenter {
        User user;
        private ShowCardView showCardView;
    public GetUserCardCompl(ShowCardView showCardView){
            this.showCardView=showCardView;
        }


    @Override
    public void getUserCard() {
        /* set资料信息*/
        showCardView.initView();

    }


    }
