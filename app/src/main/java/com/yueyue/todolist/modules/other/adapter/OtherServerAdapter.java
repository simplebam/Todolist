package com.yueyue.todolist.modules.other.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yueyue.todolist.R;
import com.yueyue.todolist.common.listener.OnItemClickListener;
import com.yueyue.todolist.modules.other.domain.ItemInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author : yueyue on 2018/3/13 21:20
 * desc   :
 */

public class OtherServerAdapter extends RecyclerView.Adapter {
    Context cxt;
    List<ItemInfo> itemInfos;
    boolean isGrid;

    public interface OnOtherServerItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnOtherServerItemClickListener onOtherServerItemClickListener;

    public void setOnOtherServerItemClickListener(OnOtherServerItemClickListener onOtherServerItemClickListener) {
        this.onOtherServerItemClickListener = onOtherServerItemClickListener;
    }

    public OtherServerAdapter(Context cxt, List<ItemInfo> itemInfos, boolean isGrid) {
        this.cxt = cxt;
        this.itemInfos = itemInfos;
        this.isGrid = isGrid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (isGrid) {
            view = LayoutInflater.from(cxt).inflate(R.layout.item_other_server_grid, parent,
                    false);
        } else {
            view = LayoutInflater.from(cxt).inflate(R.layout.item_other_server_line, parent,
                    false);
        }
        OtherServerViewHolder otherServerViewHolder = new OtherServerViewHolder(view);
        setItemOnClickEvent(otherServerViewHolder, false);
        return otherServerViewHolder;
    }

    private void setItemOnClickEvent(final OtherServerViewHolder otherServerViewHolder, boolean b) {
        otherServerViewHolder.itemView.setOnClickListener(v -> {
            if (onOtherServerItemClickListener != null) {
                onOtherServerItemClickListener.onItemClick(v, otherServerViewHolder.getLayoutPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OtherServerViewHolder otherServerViewHolder = (OtherServerViewHolder) holder;
        otherServerViewHolder.ivOperIcon.setImageResource(itemInfos.get(position).getIcon());
        otherServerViewHolder.tvOperDesc.setText(itemInfos.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return itemInfos.size();
    }

    class OtherServerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_oper_icon)
        ImageView ivOperIcon;
        @BindView(R.id.tv_oper_desc)
        TextView tvOperDesc;

        public OtherServerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

