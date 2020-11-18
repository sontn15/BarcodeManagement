package com.sh.barcodemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.SubBill;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.SubBillCartViewHolder> {

    public interface OnChildItemInCartClickListener {
        void onPlusClick(SubBill item);

        void onMinusClick(SubBill item);

        void onDeletedClick(SubBill item);

        void onItemLongClick(SubBill item);
    }


    private Context mContext;
    private Store storeCurrent;
    private List<SubBill> arrItems;
    private OnChildItemInCartClickListener listener;

    public GioHangAdapter(Context mContext,
                          Store storeCurrent,
                          List<SubBill> arrItems,
                          OnChildItemInCartClickListener listener) {
        this.mContext = mContext;
        this.storeCurrent = storeCurrent;
        this.arrItems = arrItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubBillCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_san_pham_gio_hang, parent, false);
        return new SubBillCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubBillCartViewHolder holder, int position) {
        SubBill item = arrItems.get(position);
        holder.tvItemNameCart.setText(item.getItem().getName());
        holder.tvQuantityItemCart.setText(String.valueOf(item.getQuantity()));
        holder.tvOutPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
        holder.tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(item.getTotalMoney()));
        holder.tvUnitCart.setText(item.getUnit() != null ? item.getUnit().getName() : "Không có đơn vị");

        holder.llSoLuongVienItemCart.setVisibility(View.VISIBLE);
        holder.llKichThuocItemCart.setVisibility(View.VISIBLE);
        holder.llTongSoLuongItemCart.setVisibility(View.VISIBLE);
        holder.llTotalPriceItemCart.setVisibility(View.GONE);
        holder.llDonViItemCart.setVisibility(View.GONE);

        holder.tvSoLuongVienItemCart.setText("Số tấm: " + item.getSoLuongVien());
        holder.tvTongSoLuongItemCart.setText("Kích thước còn lại: " + item.getQuantity() + " (m2)");
        holder.tvTongSoTienItemCart.setText(StringFormatUtils.convertToStringMoneyVND(item.getTotalMoney()));

        holder.bind(arrItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return arrItems.size();
    }


    public static class SubBillCartViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemNameCart, tvTotalPriceCart, tvOutPriceCart, tvQuantityItemCart, tvUnitCart;
        protected LinearLayout llSoLuongVienItemCart, llKichThuocItemCart, llDonViItemCart;
        protected LinearLayout llTotalPriceItemCart, llTongSoLuongItemCart;
        protected TextView tvSoLuongVienItemCart;
        protected TextView tvTongSoLuongItemCart, tvTongSoTienItemCart;
        protected Button btnPlus, btnMinus;
        private ImageView imvDelete;

        public SubBillCartViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlus = itemView.findViewById(R.id.btnPlusItemCart);
            btnMinus = itemView.findViewById(R.id.btnMinItemCart);
            tvUnitCart = itemView.findViewById(R.id.tvUnitItemCart);
            imvDelete = itemView.findViewById(R.id.imvDeleteItemCart);
            tvItemNameCart = itemView.findViewById(R.id.tvNameItemCart);
            tvOutPriceCart = itemView.findViewById(R.id.tvOutPriceItemCart);
            tvQuantityItemCart = itemView.findViewById(R.id.tvQuantityItemCart);
            tvTotalPriceCart = itemView.findViewById(R.id.tvTotalPriceItemCart);
            llDonViItemCart = itemView.findViewById(R.id.llDonViItemCart);
            llTotalPriceItemCart = itemView.findViewById(R.id.llTotalPriceItemCart);
        }

        public void bind(final SubBill itemInCart, final OnChildItemInCartClickListener listener) {
            btnPlus.setOnClickListener(view -> {
                double num = itemInCart.getQuantity() + 1;
                long total = (long) (num * itemInCart.getPrice());

                itemInCart.setQuantity(num);
                itemInCart.setTotalMoney(total);

                tvQuantityItemCart.setText(String.valueOf(itemInCart.getQuantity()));
                tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotalMoney()));

                listener.onPlusClick(itemInCart);
            });

            btnMinus.setOnClickListener(view -> {
                if (itemInCart.getQuantity() > 1) {
                    double num = itemInCart.getQuantity() - 1;
                    long total = (long) (num * itemInCart.getPrice());

                    itemInCart.setQuantity(num);
                    itemInCart.setTotalMoney(total);

                    tvQuantityItemCart.setText(String.valueOf(itemInCart.getQuantity()));
                    tvTotalPriceCart.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotalMoney()));

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
