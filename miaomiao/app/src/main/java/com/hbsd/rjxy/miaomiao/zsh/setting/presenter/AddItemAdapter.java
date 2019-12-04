package com.hbsd.rjxy.miaomiao.zsh.setting.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbsd.rjxy.miaomiao.R;

import java.util.List;
import java.util.Map;

public class AddItemAdapter extends BaseAdapter {
    private List<Map<String,Object>> dataSource=null;
    private Context context=null;
    private int item_layout_id;

    public AddItemAdapter(Context context,
                      List<Map<String, Object>> dataSource,
                      int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {

        if(dataSource==null){
            return 0;
        }
        else{
            return dataSource.size();
        }
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {

        return dataSource.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View newView=inflater.inflate(item_layout_id,null);

        TextView txTitle=newView.findViewById(R.id.self_title);
        ImageView imageView=newView.findViewById(R.id.self_img);

        Map<String,Object> map=dataSource.get(position);

        final  String title=map.get("title").toString();
        txTitle.setText(title);
        Resources res=context.getResources();
        switch (position){
            case 0:{
                Drawable img=res.getDrawable(R.drawable.self_mp);
                imageView.setImageDrawable(img);
                break;
            }
            case 1:{
                Drawable img=res.getDrawable(R.drawable.self_dy);
                imageView.setImageDrawable(img);
                break;
            }
            case 2:{
                Drawable img=res.getDrawable(R.drawable.self_xg);
                imageView.setImageDrawable(img);
                break;
            }
            case 3:{
                Drawable img=res.getDrawable(R.drawable.self_cx);
                imageView.setImageDrawable(img);
                break;
            }
        }


        return newView;
    }
}
