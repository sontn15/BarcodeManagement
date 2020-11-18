package com.sh.barcodemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemViewHolder> {
    private Context mContext;
    private List<Item> lstItems;
    private OnItemClickListener onItemClickListener;


    public SanPhamAdapter(Context mContext,
                          List<Item> lstItems,
                          OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.lstItems = lstItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_san_pham, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = lstItems.get(position);
        holder.tvMaSP.setText("Mã SP: " + (item.getCode() != null ? item.getCode() : ""));
        holder.tvPriceItem.setText(StringFormatUtils.convertToStringMoneyVND(item.getGiaBanLe()));
        holder.tvItemName.setText(item.getName() != null ? item.getName() : "Tên sản phẩm chưa cập nhật");
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        return lstItems.size();
    }

    public interface OnItemClickListener {
        void onClickItem(int position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemName, tvPriceItem, tvMaSP;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSP = itemView.findViewById(R.id.tvMaSPItem);
            tvItemName = itemView.findViewById(R.id.tvNameItem);
            tvPriceItem = itemView.findViewById(R.id.tvPriceItem);
        }
    }
}
