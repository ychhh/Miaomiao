package com.hbsd.rjxy.miaomiao.zlc.vedio.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hbsd.rjxy.miaomiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoAndCommentActivity extends AppCompatActivity {

    @BindView(R.id.cc_viewPager)
    ViewPager viewPager;

    List<Fragment> fragments;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catinfo_comment_layout);

        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        fragments.add(new CatinfoFragment());
        fragments.add(new CommentFragment());
        viewPager.setAdapter(new CustomPageAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));



    }

    private class CustomPageAdapter extends FragmentPagerAdapter {


        public CustomPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
