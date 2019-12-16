package com.hbsd.rjxy.miaomiao.wq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.publish.model.PublishActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CatFCActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    private TabHost tabHost;
    private ImageButton ibfanhui;
    private ImageButton ibfabu;
    private RecyclerView mRvTextList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_fc);
        findViews();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        handle(bundle);

        mRvTextList=findViewById(R.id.my_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(CatFCActivity.this);
//        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRvTextList.setLayoutManager(new LinearLayoutManager(CatFCActivity.this,RecyclerView.VERTICAL,true));
        mRvTextList.setAdapter(new TextListAdapter(this));


        try{
            tabHost = (TabHost) this.findViewById(R.id.TabHost01);
            tabHost.setup();

            tabHost.addTab(tabHost.newTabSpec("tab_1")
                    .setContent(R.id.L1)
                    .setIndicator("one",this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.addTab(tabHost.newTabSpec("tab_2")
                    .setContent(R.id.L2)
                    .setIndicator("two", this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.addTab(tabHost.newTabSpec("tab_3")
                    .setContent(R.id.L3)
                    .setIndicator("three", this.getResources().getDrawable(R.drawable.miao_head)));
            tabHost.setCurrentTab(0);
        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("EXCEPTION", ex.getMessage());
        }
    }

    //获取cid
    private void handle(Bundle bundle) {

    }

    private void  findViews(){
        ibfanhui = findViewById(R.id.fanhui);
        ibfabu = findViewById(R.id.fabu);
    }
    public void  onClick(View v){
        switch (v.getId()){
            case R.id.fanhui:
                finish();
//                Intent intent = new Intent(MainActivity.this,CatMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.fabu:
                Intent intent1 = new Intent(CatFCActivity.this, PublishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("","");
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;

        }
    }





//    private class setOnLongClickListener implements View.OnLongClickListener{
//
//        @Override
//        public boolean onLongClick(View v) {
//            Intent intent1 = new Intent(MainActivity.this,fabu_wenzi.class);
//            startActivity(intent1);
//            return true;
////            return false;
//        }
//    }
}
