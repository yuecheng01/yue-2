package com.yeucheng.yue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeucheng.yue.R;

import java.util.List;
import java.util.Map;

/**
 * Created by yuecheng on 2017/11/3.
 */

public class LeftMenuAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater mInflater;

    public LeftMenuAdapter(Context context, List<Map<String, Object>> leftMenuListData) {
        this.mInflater = LayoutInflater.from(context);
        this.data = leftMenuListData;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 1;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewholder = null;
        if (convertView == null) {
            mViewholder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.item_home_leftmenu, null);
            mViewholder.img = (ImageView) convertView.findViewById(R.id.img_i);
            mViewholder.tv = (TextView) convertView.findViewById(R.id.tv_i);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(mViewholder);
        } else {
            mViewholder = (ViewHolder) convertView.getTag();
        }
        mViewholder.getImg().setImageResource((Integer) data.get(position).get("img"));
        mViewholder.getTv().setText((String) data.get(position).get("title"));
        return convertView;
    }

    public void upData(List<Map<String, Object>> getdata) {
        this.data = getdata;
        notifyDataSetChanged();
    }

    //ViewHolder静态类
    class ViewHolder {
        private ImageView img;
        private TextView tv;

        public ImageView getImg() {
            return img;
        }

        public void setImg(ImageView img) {
            this.img = img;
        }

        public TextView getTv() {
            return tv;
        }

        public void setTv(TextView tv) {
            this.tv = tv;
        }
    }
}
