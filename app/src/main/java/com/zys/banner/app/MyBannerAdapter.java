package com.zys.banner.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zys.banner.BannerAdapter;

import java.util.List;
import java.util.zip.Inflater;

public class MyBannerAdapter extends BannerAdapter<String, MyBannerAdapter.MyViewHolder> {
    public MyBannerAdapter(List<String> datas) {
        super(datas);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.updateView(datas.get(position), position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void updateView(String s, int pos) {
            TextView tv1 = itemView.findViewById(R.id.tv_1);
            tv1.setText("data = " + s);
            TextView tv2 = itemView.findViewById(R.id.tv_2);
            tv2.setText("position = " + pos);
        }
    }
}
