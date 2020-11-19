package com.sh.barcodemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import java.util.List;

public class ItemInCartAdapter extends RecyclerView.Adapter<ItemInCartAdapter.ItemCartViewHolder> {

    public interface OnChildItemInCartClickListener {
        void onPlusClick(ItemInCart item);

        void onMinusClick(ItemInCart item);

        void onDeletedClick(ItemInCart item);

        void onItemLongClick(ItemInCart item);
    }


    private Context mContext;
    private List<ItemInCart> arrItems;
    private OnChildItemInCartClickListener listener;

    public ItemInCartAdapter(Context mContext, List<ItemInCart> arrItems, OnChildItemInCartClickListener listener) {
        this.mContext = mContext;
        this.arrItems = arrItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_good_cart, parent, false);
        return new ItemCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCartViewHolder holder, int position) {
        ItemInCart item = arrItems.get(position);
        holder.tvItemNameCart.setText(item.getItem().getName());
        holder.tvUnitCart.setText(item.getUnitChoose() != null ? item.getUnitChoose().getName() : "Không có đơn vị");
        holder.tvQuantityItemCart.setText(String.valueOf(item.getQuantity()));
        holder.tvOutPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
        holder.tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(item.getTotal()));

        holder.bind(arrItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return arrItems.size();
    }


    public static class ItemCartViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemNameCart, tvTotalPriceCart, tvOutPriceCart, tvQuantityItemCart, tvUnitCart;
        protected Button btnPlus, btnMinus;
        private ImageView imvDelete;

        public ItemCartViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlus = itemView.findViewById(R.id.btnPlusItemCart);
            btnMinus = itemView.findViewById(R.id.btnMinItemCart);
            tvUnitCart = itemView.findViewById(R.id.tvUnitItemCart);
            imvDelete = itemView.findViewById(R.id.imvDeleteItemCart);
            tvItemNameCart = itemView.findViewById(R.id.tvNameItemCart);
            tvOutPriceCart = itemView.findViewById(R.id.tvOutPriceItemCart);
            tvQuantityItemCart = itemView.findViewById(R.id.tvQuantityItemCart);
            tvTotalPriceCart = itemView.findViewById(R.id.tvTotalPriceItemCart);
        }

        public void bind(final ItemInCart itemInCart, final OnChildItemInCartClickListener listener) {
            btnPlus.setOnClickListener(view -> {
                long num = itemInCart.getQuantity() + 1;
                long total = num * itemInCart.getPrice();

                itemInCart.setQuantity(num);
                itemInCart.setTotal(total);

                tvQuantityItemCart.setText(String.valueOf(itemInCart.getQuantity()));
                tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotal()));

                listener.onPlusClick(itemInCart);
            });

            btnMinus.setOnClickListener(view -> {
                if (itemInCart.getQuantity() > 1) {
                    long num = itemInCart.getQuantity() - 1;
                    long total = num * itemInCart.getPrice();

                    itemInCart.setQuantity(num);
                    itemInCart.setTotal(total);

                    tvQuantityItemCart.setText(String.valueOf(itemInCart.getQuantity()));
                    tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotal()));

                }
                listener.onMinusClick(itemInCart);
            });

            imvDelete.setOnClickListener(view -> {
                listener.onDeletedClick(itemInCart);
            });

            itemView.setOnLongClickListener(v -> {
                listener.onItemLongClick(itemInCart);
                return true;
            });
        }
    }
}
