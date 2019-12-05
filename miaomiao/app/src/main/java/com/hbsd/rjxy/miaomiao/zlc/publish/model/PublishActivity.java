package com.hbsd.rjxy.miaomiao.zlc.publish.model;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbsd.rjxy.miaomiao.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublishActivity extends AppCompatActivity {

    @BindView(R.id.et_edit)
    EditText etEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_layout);

        ButterKnife.bind(this);

        etEdit.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.e("type",""+bundle.getSerializable("type"));
        Log.e("url",""+bundle.getSerializable("url"));


    }
}
