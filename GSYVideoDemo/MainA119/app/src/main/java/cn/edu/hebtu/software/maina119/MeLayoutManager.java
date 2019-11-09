package cn.edu.hebtu.software.maina119;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MeLayoutManager extends LinearLayoutManager {

    private PagerSnapHelper pagerSnapHelper;
    private RecyclerView recyclerView;

    public MeLayoutManager(Context context) {
        super(context);
        init();
    }

    private void init() {
        pagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        pagerSnapHelper.attachToRecyclerView(view);
        this.recyclerView = view;
    }
}
