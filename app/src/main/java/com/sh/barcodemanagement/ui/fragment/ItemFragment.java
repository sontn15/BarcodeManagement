package com.sh.barcodemanagement.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.adapter.ItemAdapter;
import com.sh.barcodemanagement.database.BarcodeDatabase;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.database.entity.ItemEntity;
import com.sh.barcodemanagement.database.entity.UnitEntity;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Result;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.network.BarcodeApiService;
import com.sh.barcodemanagement.network.RetrofitClient;
import com.sh.barcodemanagement.ui.activity.CartActivity;
import com.sh.barcodemanagement.ui.activity.ScannerActivity;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.NetworkUtils;
import com.sh.barcodemanagement.utils.ResourceUtils;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFragment extends Fragment implements View.OnClickListener, OnSpinnerItemSelectedListener {
    private View mView;
    public static Button btnTotal;
    private EditText edtSearch;
    private ImageView imvScanner;
    private RecyclerView rcvItem;
    private ProgressDialog progressDialog;

    private View dialog;
    private Button btnSubmitAddDlg;
    private NiceSpinner unitSpinner;
    private AlertDialog addItemDialog;
    private ImageView btnPlusDlg, btnMinusDlg;
    private TextView tvNameDlg, tvTotalPriceDlg, tvPriceDlg, tvQuantityDlg;

    private Store storeLogin;
    private ItemInCart itemInCart;
    private List<Item> listItems;
    private ItemAdapter itemAdapter;
    private Map<Long, UnitEntity> unitEntityMap;

    private MySharedPreferences preferences;
    private BarcodeApiService barcodeApiService;

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        preferences = new MySharedPreferences(requireContext());
        storeLogin = preferences.getStore(Const.KEY_SHARE_PREFERENCE.KEY_STORE);
        if (preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            List<ItemInCart> lstItemCart = preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART);
            calculatorCurrentTotalPrice(lstItemCart);
        }
        listItems = new ArrayList<>();
        barcodeApiService = RetrofitClient.getClient().create(BarcodeApiService.class);
        unitEntityMap = BarcodeDatabase.getInstance(requireActivity()).barcodeDAO().findAllUnits()
                .stream().collect(Collectors.toMap(UnitEntity::getId, obj -> obj));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAdapter() {
        itemAdapter = new ItemAdapter(requireContext(), listItems, item -> {
            buildDialog(item);
            ResourceUtils.showAlertDialog(addItemDialog);
        });
        rcvItem.setAdapter(itemAdapter);

        ResourceUtils.showProgressDialog(progressDialog);
        if (NetworkUtils.haveNetwork(requireActivity())) {
            Call<Result> callChange = barcodeApiService.checkDataIsChange(storeLogin.getId());
            callChange.enqueue(new Callback<Result>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result result = response.body();
                        if (result.getResult().equalsIgnoreCase("true")) {
                            Log.d(Const.LOG_TAG, "Have new data, sync data start...");
                            ResourceUtils.syncDataFromServer(
                                    requireActivity(), progressDialog, barcodeApiService, storeLogin, listItems, itemAdapter, null);
                        } else {
                            listItems.clear();
                            listItems.addAll(ResourceUtils.getAllItemFromRoomDatabase(requireActivity()));
                            itemAdapter.setData(listItems);
                            ResourceUtils.hiddenProgressDialog(progressDialog);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    ResourceUtils.hiddenProgressDialog(progressDialog);
                }
            });
        } else {
            listItems.clear();
            listItems.addAll(ResourceUtils.getAllItemFromRoomDatabase(requireActivity()));
            itemAdapter.setData(listItems);
            ResourceUtils.hiddenProgressDialog(progressDialog);
            Log.d(Const.LOG_TAG, "LIST ITEM GET IN DATABASE WHEN NOT NETWORK = " + listItems.size());
        }
    }

    private void initView() {
        edtSearch = mView.findViewById(R.id.edtSearchBanHang);
        imvScanner = mView.findViewById(R.id.imvBarCodeBanHang);
        btnTotal = mView.findViewById(R.id.btnTotal);

        rcvItem = mView.findViewById(R.id.rcvSanPhamBanHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvItem.setItemAnimator(new DefaultItemAnimator());
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
        dialog = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        tvNameDlg = dialog.findViewById(R.id.tvItemNameDialog);
        btnPlusDlg = dialog.findViewById(R.id.btnItemPlusDialog);
        tvPriceDlg = dialog.findViewById(R.id.tvPriceItemDialog);
        btnMinusDlg = dialog.findViewById(R.id.btnItemMinusDialog);
        unitSpinner = dialog.findViewById(R.id.spnDonViItemDialog);
        btnSubmitAddDlg = dialog.findViewById(R.id.btnAddItemDialog);
        tvTotalPriceDlg = dialog.findViewById(R.id.tvTotalPriceDialog);
        tvQuantityDlg = dialog.findViewById(R.id.tvItemNumOrderDialog);
        addItemDialog.setView(dialog);

        btnPlusDlg.setOnClickListener(this);
        btnMinusDlg.setOnClickListener(this);
        btnSubmitAddDlg.setOnClickListener(this);
        unitSpinner.setOnSpinnerItemSelectedListener(this);
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
                onClickPlusDialog();
                break;
            }
            case R.id.btnItemMinusDialog: {
                onClickMinusDialog();
                break;
            }
            case R.id.btnAddItemDialog: {
                onClickSubmitDialog();
                break;
            }
        }
    }

    private void onClickBarCodeScanner() {
        Intent intent = new Intent(requireActivity(), ScannerActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE.REQUEST_CODE_SCANNER);
    }

    private void onClickButtonTotal() {
        if (preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            startActivity(new Intent(requireActivity(), CartActivity.class));
        } else {
            showSnackBar(getResources().getString(R.string.please_add_item_in_cart));
        }
    }

    private void onClickPlusDialog() {
        long num = Long.parseLong(tvQuantityDlg.getText().toString()) + 1;
        long totalPrice = num * itemInCart.getPrice();

        itemInCart.setQuantity(num);
        itemInCart.setTotal(totalPrice);

        tvQuantityDlg.setText(String.valueOf(num));
        tvTotalPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    private void onClickMinusDialog() {
        long num = 1;
        long quantityCurrent = Long.parseLong(tvQuantityDlg.getText().toString());
        if (quantityCurrent > 1) {
            num = quantityCurrent - 1;
        }
        long totalPrice = num * itemInCart.getPrice();

        itemInCart.setQuantity(num);
        itemInCart.setTotal(totalPrice);

        tvQuantityDlg.setText(String.valueOf(num));
        tvTotalPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    private void onClickSubmitDialog() {
        List<ItemInCart> lstItemCart = new ArrayList<>();
        if (preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            lstItemCart.addAll(preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART));
        }
        if (lstItemCart.size() > 0) {
            ItemInCart itemRemove = null;
            for (ItemInCart obj : lstItemCart) {
                if (itemInCart.getId().equals(obj.getItem().getId())
                        && itemInCart.getUnitChoose().getId().equals(obj.getUnitChoose().getId())) {
                    itemRemove = obj;
                }
            }
            if (itemRemove != null) {
                lstItemCart.remove(itemRemove);
            }
        }
        lstItemCart.add(itemInCart);
        preferences.putListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);
        calculatorCurrentTotalPrice(lstItemCart);
        ResourceUtils.hiddenAlertDialog(addItemDialog);
    }

    @Override
    public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
        Unit unitChoose = (Unit) parent.getItemAtPosition(position);
        long quyCach = 1;
        long giaQuyDoi = 0;
        if (itemInCart.getItem().getUnit1Obj() != null && itemInCart.getItem().getUnit1Obj().getId().equals(unitChoose.getId())) {
            quyCach = itemInCart.getItem().getQuyCach1();
            giaQuyDoi = itemInCart.getItem().getGiaQuyDoi1();
        }
        if (itemInCart.getItem().getUnit2Obj() != null && itemInCart.getItem().getUnit2Obj().getId().equals(unitChoose.getId())) {
            quyCach = itemInCart.getItem().getQuyCach2();
            giaQuyDoi = itemInCart.getItem().getGiaQuyDoi2();
        }
        if (itemInCart.getItem().getUnitMinObj() != null && itemInCart.getItem().getUnitMinObj().getId().equals(unitChoose.getId())) {
            quyCach = 1;
            giaQuyDoi = itemInCart.getItem().getGiaBan();
        }
        long quantity = Long.parseLong(tvQuantityDlg.getText().toString());
        long totalPrice = giaQuyDoi * quantity;

        itemInCart.setQuantity(quantity);
        itemInCart.setUnitChoose(unitChoose);
        itemInCart.setPrice(giaQuyDoi);
        itemInCart.setTotal(totalPrice);
        itemInCart.setHeSoCoSo(quyCach);

        tvPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(giaQuyDoi));
        tvTotalPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE.REQUEST_CODE_SCANNER) {
            ItemEntity itemEntity;
            String barcode = data.getStringExtra("BARCODE");
            itemEntity = BarcodeDatabase.getInstance(requireActivity()).barcodeDAO().findItemEntityByBarcode(barcode.trim());
            if (itemEntity == null) {
                itemEntity = BarcodeDatabase.getInstance(requireActivity()).barcodeDAO().findItemEntityByBarcodeSub(barcode.trim());
            }
            if (itemEntity != null) {
                buildDialog(ResourceUtils.convertItemEntityToItem(unitEntityMap, itemEntity));
                ResourceUtils.showAlertDialog(addItemDialog);
            } else {
                showSnackBar(getResources().getString(R.string.khong_tim_thay_sp));
            }
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ResourceUtils.destroyAlertDialog(addItemDialog);
        ResourceUtils.destroyProgressDialog(progressDialog);
    }

    private void buildDialog(Item itemClick) {
        itemInCart = new ItemInCart();
        itemInCart = ItemInCart.builder().id(itemClick.getId()).quantity(1L).item(itemClick).price(itemClick.getGiaBan()).total(itemClick.getGiaBan())
                .unitChoose(itemClick.getUnitDefaultObj() != null ? itemClick.getUnitDefaultObj() : new Unit())
                .unitCoSo(itemClick.getUnitMin()).heSoCoSo(1L).heSoCoSo(ResourceUtils.buildHeSoCoSoForItemInCart(itemClick)).build();

        tvQuantityDlg.setText(String.valueOf(1L));
        tvPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(itemClick.getGiaBan()));
        tvTotalPriceDlg.setText(StringFormatUtils.convertToStringMoneyVND(itemInCart.getTotal()));
        tvNameDlg.setText(itemInCart.getItem().getName() != null ? itemInCart.getItem().getName() : "");

        List<Unit> lstUnits = new ArrayList<>(ResourceUtils.buildListUnitInItem(itemInCart));
        unitSpinner.attachDataSource(lstUnits);
        unitSpinner.setSelectedIndex(0);
    }


    public static void calculatorCurrentTotalPrice(List<ItemInCart> lstItemCart) {
        long totalPrice = 0L;
        if (lstItemCart != null && !lstItemCart.isEmpty()) {
            for (ItemInCart obj : lstItemCart) {
                totalPrice += obj.getTotal();
            }
        }
        btnTotal.setText(lstItemCart.size() + " sản phẩm = " + StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }
}
