package com.example.tripapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // 用来加载网络图片
import com.example.tripapp.Bean.PopularItem;
import com.example.tripapp.R;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    private Context context;
    private List<PopularItem> popularList;

    // 构造函数
    public PopularAdapter(Context context, List<PopularItem> popularList) {
        this.context = context;
        this.popularList = popularList;
    }

    // ViewHolder：保存 item 中的控件引用
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic,ivLove;
        TextView tvTitle, tvAddress, tvPrice,tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.shapeableImageView);
            ivLove = itemView.findViewById(R.id.iv_love);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAddress = itemView.findViewById(R.id.tv_location);
            tvPrice = itemView.findViewById(R.id.tv_money);
            tvScore = itemView.findViewById(R.id.tv_star);
        }
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建 item 视图
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        // 绑定数据
        PopularItem item = popularList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAddress.setText(item.getAddress());
        holder.tvScore.setText(item.getScore() + "");
        holder.tvPrice.setText("￥" + item.getPrice());
        holder.ivLove.setOnClickListener(v -> {
            holder.ivLove.setSelected(!holder.ivLove.isSelected());
        });

        // 使用 Glide 加载网络图片
        Glide.with(context)
                .load(item.getPic().get(0))
                .into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }
}
