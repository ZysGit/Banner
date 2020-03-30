package com.zys.banner;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> datas = new ArrayList<>();
    private OnBannerListener<T> onBannerListener;

    public BannerAdapter(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        if (!datas.isEmpty()) {
            this.datas.add(0, datas.get(datas.size() - 1));
            this.datas.add(0, datas.get(datas.size() < 2 ? 0 : datas.size() - 2));
            this.datas.add(this.datas.size(), datas.get(0));
            this.datas.add(this.datas.size(), datas.get(datas.size() < 2 ? 0 : 1));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        final int realPosition = BannerUtils.getRealPosition(position, datas.size() - 4);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerListener != null) {
                    onBannerListener.onBannerClicked(datas.get(position), realPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    void setOnBannerListener(OnBannerListener<T> onBannerListener) {
        this.onBannerListener = onBannerListener;
    }
}
