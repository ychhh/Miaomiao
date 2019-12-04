package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbsd.rjxy.miaomiao.R;

import org.w3c.dom.Text;

public class EditProfileActivity extends AppCompatActivity {
    private Button btn_back;
    private Button btn_commit;
    private TextView tx_reName;
    private ImageView tx_reImg;
    private TextView tx_reSbp;
    private TextView tx_reSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btn_back=findViewById(R.id.self_back);
        btn_commit=findViewById(R.id.self_reCommit);
        tx_reImg=findViewById(R.id.self_reImg);
        tx_reName=findViewById(R.id.self_reName);
        tx_reSbp=findViewById(R.id.self_reSbp);
        tx_reSex=findViewById(R.id.self_reSex);
        //大监听
        ButtonClickLinstener buttonClickLinstener=new ButtonClickLinstener();
        //绑定监听
        btn_back.setOnClickListener(buttonClickLinstener);
        btn_commit.setOnClickListener(buttonClickLinstener);




    }
    public class ButtonClickLinstener implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.self_back:{
                    finish();
                    break;
                }
                case R.id.self_reCommit:{

                    String newName=tx_reName.getText().toString();
                    String newsbp=tx_reSbp.getText().toString();
                    String newSex=tx_reSex.getText().toString();
                    Log.e("新的用户名",newName);
                    /*将数据插入数据库*/
                    finish();

                    break;

                }

            }
        }
    }
}
