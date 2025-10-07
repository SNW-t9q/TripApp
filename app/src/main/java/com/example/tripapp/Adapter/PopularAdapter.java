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

    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_GRID = 1;
    private Context context;
    private List<PopularItem> popularList;
    private boolean isGrid;

    // 构造函数
    public PopularAdapter(Context context, List<PopularItem> popularList, boolean isGrid) {
        this.context = context;
        this.popularList = popularList;
        this.isGrid = isGrid;
    }

    public void setGrid(boolean isGrid) {
        this.isGrid = isGrid;
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

    @Override
    public int getItemViewType(int position) {
        return isGrid ? VIEW_TYPE_GRID : VIEW_TYPE_LIST;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建 item 视图
        View view;
        if (viewType == VIEW_TYPE_LIST) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_list, parent, false);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_grid, parent, false);
        }
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
        if(item.getPic() != null && !item.getPic().isEmpty()){
            Glide.with(context)
                    .load(item.getPic().get(0))
                    .error(R.drawable.loading)
                    .into(holder.ivPic);
        }else {
            Glide.with(context)
                    .load(R.drawable.loading)
                    .into(holder.ivPic);
        }

    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }
}
