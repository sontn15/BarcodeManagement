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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {
    private final Context mContext;
    private final List<Item> lstItems;
    private List<Item> lstFiltered;
    private final OnItemClickListener onItemClickListener;


    public ItemAdapter(Context mContext,
                       List<Item> lstItems,
                       OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.lstItems = lstItems;
        this.lstFiltered = lstItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_good, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = lstFiltered.get(position);

        holder.tvPriceItem.setText(StringFormatUtils.convertToStringMoneyVND(item.getGiaBanLe()));
        holder.tvItemName.setText(item.getName() != null ? item.getName() : "Tên sản phẩm chưa cập nhật");
        holder.tvDonVi.setText((item.getUnitDefaultObj() != null && item.getUnitDefaultObj().getName() != null) ?
                (item.getUnitDefaultObj().getName()) : "Không có đơn vị mặc định");

        holder.itemView.setOnClickListener(view -> onItemClickListener.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        if (lstItems != null) {
            return lstFiltered.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        void onClickItem(int position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvItemName, tvPriceItem, tvDonVi;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonVi = itemView.findViewById(R.id.tvDonViItem);
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
                    String code = obj.getCode() != null ? StringFormatUtils.convertUTF8ToString(obj.getCode().trim().toLowerCase()) : "";
                    String name = obj.getName() != null ? StringFormatUtils.convertUTF8ToString(obj.getName().trim().toLowerCase()) : "";
                    String barCode = obj.getBarcode() != null ? StringFormatUtils.convertUTF8ToString(obj.getBarcode().trim().toLowerCase()) : "";
                    assert str != null;
                    if ((code != null && code.contains(str))
                            || (name != null && name.contains(str))
                            || (barCode != null && barCode.contains(str))) {
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
