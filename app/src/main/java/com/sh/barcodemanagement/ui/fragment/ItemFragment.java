package com.sh.barcodemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.adapter.ItemAdapter;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.ui.activity.CartActivity;
import com.sh.barcodemanagement.ui.activity.ScannerActivity;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.ResourceUtils;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment implements View.OnClickListener, OnSpinnerItemSelectedListener {
    private View mView;
    private Button btnTotal;
    private EditText edtSearch;
    private ImageView imvScanner;
    private RecyclerView rcvItem;
    private ProgressDialog progressDialog;

    private View mViewDialog;
    private NiceSpinner unitSpinner;
    private AlertDialog addItemDialog;
    private Button btnItemMinusDialog, btnItemPlusDialog, btnAddItemDialog;
    private TextView tvItemNameDialog, tvTotalPriceDialog, tvPriceItemDialog, tvItemNumOrderDialog;

    private ItemInCart itemInCart;
    private List<Item> listItems;
    private ItemAdapter itemAdapter;
    private MySharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_items, container, false);
        initView();
        initData();
        initAdapter();
        initDialogAddItem();
        return mView;
    }

    private void initData() {
        listItems = new ArrayList<>();
        preferences = new MySharedPreferences(requireContext());

        listItems.addAll(ResourceUtils.buildListItems());
    }

    private void initView() {
        edtSearch = mView.findViewById(R.id.edtSearchBanHang);
        imvScanner = mView.findViewById(R.id.imvBarCodeBanHang);
        btnTotal = mView.findViewById(R.id.btnTotal);

        rcvItem = mView.findViewById(R.id.rcvSanPhamBanHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvItem.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        imvScanner.setOnClickListener(this);
        btnTotal.setOnClickListener(this);
        btnTotal.setTransformationMethod(null);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDialogAddItem() {
        addItemDialog = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog).create();
        mViewDialog = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        unitSpinner = mViewDialog.findViewById(R.id.spnDonViItemDialog);
        tvItemNameDialog = mViewDialog.findViewById(R.id.tvItemNameDialog);
        btnAddItemDialog = mViewDialog.findViewById(R.id.btnAddItemDialog);
        btnItemPlusDialog = mViewDialog.findViewById(R.id.btnItemPlusDialog);
        tvPriceItemDialog = mViewDialog.findViewById(R.id.tvPriceItemDialog);
        tvTotalPriceDialog = mViewDialog.findViewById(R.id.tvTotalPriceDialog);
        btnItemMinusDialog = mViewDialog.findViewById(R.id.btnItemMinusDialog);
        tvItemNumOrderDialog = mViewDialog.findViewById(R.id.tvItemNumOrderDialog);
        addItemDialog.setView(mViewDialog);

        btnAddItemDialog.setOnClickListener(this);
        btnItemPlusDialog.setOnClickListener(this);
        btnItemMinusDialog.setOnClickListener(this);
        unitSpinner.setOnSpinnerItemSelectedListener(this);

        btnAddItemDialog.setTransformationMethod(null);
        btnItemPlusDialog.setTransformationMethod(null);
    }

    private void initAdapter() {
        itemAdapter = new ItemAdapter(requireContext(), listItems, position -> {
            //Set data for dialog
            Item itemClick = listItems.get(position);
            itemInCart = ItemInCart.builder().id(itemClick.getId()).quantity(1L).item(itemClick)
                    .unitChoose(itemClick.getUnitDefaultObj() != null ? itemClick.getUnitDefaultObj() : new Unit())
                    .unitCoSo(itemClick.getUnitMin()).heSoCoSo(1L).heSoCoSo(ResourceUtils.buildHeSoCoSoForItemInCart(itemClick)).build();
            Long price = ResourceUtils.getPriceDefaultOfItemForCustomer(itemInCart.getItem());
            itemInCart.setPrice(price);
            itemInCart.setTotal(price);

            tvItemNumOrderDialog.setText(String.valueOf(1L));
            tvPriceItemDialog.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getPrice()));
            tvTotalPriceDialog.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotal()));
            tvItemNameDialog.setText(itemInCart.getItem().getName() != null ? itemInCart.getItem().getName() : "");

            List<Unit> lstUnits = new ArrayList<>(ResourceUtils.buildListUnitInItem(itemInCart));
            unitSpinner.attachDataSource(lstUnits);
            unitSpinner.setSelectedIndex(0);

            ResourceUtils.showAlertDialog(addItemDialog);
        });
        rcvItem.setAdapter(itemAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBarCodeBanHang: {
                onClickBarCodeScanner();
                break;
            }
            case R.id.btnTotal: {
                onClickButtonTotal();
                break;
            }
            case R.id.btnItemPlusDialog: {
                onClickPlusButtonDialog();
                break;
            }
            case R.id.btnItemMinusDialog: {
                onClickMinusButtonDialog();
                break;
            }
            case R.id.btnAddItemDialog: {
                onClickAddItemDialog();
                break;
            }
        }
    }

    private void onClickButtonTotal() {
        startActivity(new Intent(requireActivity(), CartActivity.class));
    }

    private void onClickBarCodeScanner() {
        startActivity(new Intent(requireActivity(), ScannerActivity.class));
    }

    private void onClickPlusButtonDialog() {
        long num = Long.parseLong(tvItemNumOrderDialog.getText().toString()) + 1;
        long totalPrice = num * itemInCart.getPrice();

        itemInCart.setQuantity(num);
        itemInCart.setTotal(totalPrice);

        tvItemNumOrderDialog.setText(String.valueOf(num));
        tvTotalPriceDialog.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    private void onClickMinusButtonDialog() {
        long num = 1;
        long quantityCurrent = Long.parseLong(tvItemNumOrderDialog.getText().toString());
        if (quantityCurrent > 1) {
            num = quantityCurrent - 1;
        }
        long totalPrice = num * itemInCart.getPrice();

        itemInCart.setQuantity(num);
        itemInCart.setTotal(totalPrice);

        tvItemNumOrderDialog.setText(String.valueOf(num));
        tvTotalPriceDialog.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    private void onClickAddItemDialog() {
        List<ItemInCart> lstItemCart = new ArrayList<>();
        if (preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            lstItemCart.addAll(preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART));
        }
        if (lstItemCart.size() > 0) {
            ItemInCart itemRemove = null;
            for (ItemInCart obj : lstItemCart) {
                if (itemInCart.getId().equals(obj.getItem().getId())
                        && itemInCart.getUnitChoose().getMaUnit().equals(obj.getUnitChoose().getMaUnit())) {
                    itemRemove = obj;
                }
            }
            if (itemRemove != null) {
                lstItemCart.remove(itemRemove);
            }
        }
        lstItemCart.add(itemInCart);
        preferences.putListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);

        long totalPrice = 0L;
        if (lstItemCart != null && !lstItemCart.isEmpty()) {
            for (ItemInCart obj : lstItemCart) {
                totalPrice += obj.getTotal();
            }
        }
        String text = lstItemCart.size() + " sản phẩm = " + StringFormatUtils.convertToStringMoneyVND(totalPrice);
        btnTotal.setText(text);
        ResourceUtils.hiddenAlertDialog(addItemDialog);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ResourceUtils.destroyAlertDialog(addItemDialog);
    }

    @Override
    public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
        Unit unitChoose = (Unit) parent.getItemAtPosition(position);
        long quyCach = 1;
        long giaQuyDoi = 0;
        if (itemInCart.getItem().getUnit1Obj() != null && itemInCart.getItem().getUnit1Obj().getMaUnit().equals(unitChoose.getMaUnit())) {
            quyCach = itemInCart.getItem().getQuyCach1();
            giaQuyDoi = itemInCart.getItem().getGiaQuyDoi1();
        }
        if (itemInCart.getItem().getUnitMinObj() != null && itemInCart.getItem().getUnitMinObj().getMaUnit().equals(unitChoose.getMaUnit())) {
            quyCach = 1;
            giaQuyDoi = itemInCart.getItem().getGiaBanLe();
        }
        long quantity = Long.parseLong(tvItemNumOrderDialog.getText().toString());
        long totalPrice = giaQuyDoi * quantity;

        itemInCart.setQuantity(quantity);
        itemInCart.setUnitChoose(unitChoose);
        itemInCart.setPrice(giaQuyDoi);
        itemInCart.setTotal(totalPrice);
        itemInCart.setHeSoCoSo(quyCach);

        tvPriceItemDialog.setText(StringFormatUtils.convertToStringMoneyVND(giaQuyDoi));
        tvTotalPriceDialog.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

}
