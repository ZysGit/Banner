package com.zys.banner;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> datas;
    private BannerItemClickListener<T> bannerItemClickListener;

    public BannerAdapter(List<T> datas) {
        this.datas = new ArrayList<>();
        this.datas.addAll(datas);
        if (!datas.isEmpty()) {
            this.datas.add(0, datas.get(datas.size() - 1));
            this.datas.add(0, datas.get(datas.size() - 2));
            this.datas.add(this.datas.size(), datas.get(0));
            this.datas.add(this.datas.size(), datas.get(1));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final int realPosition = getRealPosition(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerItemClickListener != null) {
                    bannerItemClickListener.onClick(datas.get(position), realPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private int getRealPosition(int position) {
        return 0;
    }

    public void setBannerItemClickListener(BannerItemClickListener<T> bannerItemClickListener) {
        this.bannerItemClickListener = bannerItemClickListener;
    }
}
