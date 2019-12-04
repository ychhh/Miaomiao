package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbsd.rjxy.miaomiao.R;
import com.hbsd.rjxy.miaomiao.zlc.publish.model.PublishActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatinfoFragment extends Fragment {

    @BindView(R.id.tv_publish)
    TextView tvPublish;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catinfo_fragment,null);
        ButterKnife.bind(this,view);

        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PublishActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
