package com.sh.barcodemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchSanPhamAdapter extends RecyclerView.Adapter<SearchSanPhamAdapter.SearchSanPhamViewHolder> implements Filterable {
    private Context mContext;
    private List<Item> lstItems;
    private List<Item> lstFiltered;
    private OnItemClickListener onItemClickListener;

    public SearchSanPhamAdapter(Context mContext,
                                List<Item> lstItems,
                                OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.lstItems = lstItems;
        this.lstFiltered = lstItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchSanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_san_pham, parent, false);
        return new SearchSanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSanPhamViewHolder holder, int position) {
        Item item = lstFiltered.get(position);
        holder.tvMaSP.setText("Mã SP: " + (item.getCode() != null ? item.getCode() : ""));
        holder.tvPriceItem.setText(StringFormatUtils.convertToStringMoneyVND(item.getGiaBanLe()));
        holder.tvItemName.setText(item.getName() != null ? item.getName() : "Tên sản phẩm chưa cập nhật");
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClickItem(item));
    }

    @Override
    public int getItemCount() {
        if (lstFiltered != null) {
            return lstFiltered.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        void onClickItem(Item item);
    }

    public static class SearchSanPhamViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemName, tvPriceItem, tvMaSP;

        public SearchSanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSP = itemView.findViewById(R.id.tvMaSPItem);
            tvItemName = itemView.findViewById(R.id.tvNameItem);
            tvPriceItem = itemView.findViewById(R.id.tvPriceItem);
        }
    }

    /**
     * Filter for search in EditText
     */
    private Filter mFilter;

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchFilters();
        }
        return mFilter;
    }

    //filter class
    private class SearchFilters extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence str) {
            FilterResults results = new FilterResults();
            if (str == null || str.length() == 0) {
                lstFiltered = lstItems;
            } else {
                str = StringFormatUtils.convertUTF8ToString(str.toString().trim().toLowerCase());

                List<Item> lstRecordFilters = new ArrayList<>();
                for (Item obj : lstItems) {
                    String name = obj.getName() != null ? StringFormatUtils.convertUTF8ToString(obj.getName().trim().toLowerCase()) : "";
                    String code = obj.getCode() != null ? StringFormatUtils.convertUTF8ToString(obj.getCode().trim().toLowerCase()) : "";
                    String giaBanLe = obj.getGiaBanLe() != null ? StringFormatUtils.convertUTF8ToString(String.valueOf(obj.getGiaBanLe())) : "";
                    assert str != null;
                    if ((name != null && name.contains(str.toString()))
                            || (code != null && code.contains(str.toString()))
                            || (giaBanLe != null && giaBanLe.contains(str.toString()))) {
                        lstRecordFilters.add(obj);
                    }
                }
                lstFiltered = lstRecordFilters;
            }
            results.values = lstFiltered;
            results.count = lstFiltered.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstFiltered = (List<Item>) results.values;
            notifyDataSetChanged();
        }
    }

}