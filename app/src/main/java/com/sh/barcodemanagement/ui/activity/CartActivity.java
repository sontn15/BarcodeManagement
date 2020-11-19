package com.sh.barcodemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.adapter.ItemInCartAdapter;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.ui.fragment.SettingFragment;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.ResourceUtils;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOrder;
    private TextView tvTotalPrice;
    private RecyclerView rcvItems;
    private ProgressDialog progressDialog;
    private List<ItemInCart> lstItemCart;
    private ItemInCartAdapter itemInCartAdapter;

    private View mViewItemEditDialog;
    private AlertDialog itemEditDialog;
    private EditText edtPriceChangeItemEditDlg;
    private Button btnCancelItemEditDlg, btnApproveItemEditDlg;
    private TextView tvNameItemEditDlg, tvPriceCurrentItemEditDlg, tvUnitItemEditDlg;

    private Store store;
    private Long totalPrice = 0L;
    private ItemInCart itemInCartEdit;
    private MySharedPreferences preferences;

    private Receiver netReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initData();
        initAdapter();
        calculatorCurrentTotalPrice();
        initPrinter();
    }

    private void initData() {
        preferences = new MySharedPreferences(getApplicationContext());
        lstItemCart = new ArrayList<>();
        if (preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART) != null) {
            lstItemCart.addAll(preferences.getListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART));
        }
        store = preferences.getStore(Const.KEY_SHARE_PREFERENCE.KEY_STORE);
    }

    private void initView() {
        rcvItems = findViewById(R.id.rcvItemsCart);
        btnOrder = findViewById(R.id.btnOrderCart);
        tvTotalPrice = findViewById(R.id.tvTotalPriceCart);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvItems.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        //edit price dialog
        itemEditDialog = new AlertDialog.Builder(getApplicationContext(), R.style.CustomAlertDialog).create();
        mViewItemEditDialog = getLayoutInflater().inflate(R.layout.dialog_edit_item_in_cart, null);
        tvNameItemEditDlg = mViewItemEditDialog.findViewById(R.id.tvNameItemEditMoneyDlg);
        tvUnitItemEditDlg = mViewItemEditDialog.findViewById(R.id.tvUnitItemEditMoneyDlg);
        btnCancelItemEditDlg = mViewItemEditDialog.findViewById(R.id.btnCancelEditMoneyDlg);
        btnApproveItemEditDlg = mViewItemEditDialog.findViewById(R.id.btnApproveEditMoneyDlg);
        edtPriceChangeItemEditDlg = mViewItemEditDialog.findViewById(R.id.edtPriceChangeItemEditMoneyDlg);
        tvPriceCurrentItemEditDlg = mViewItemEditDialog.findViewById(R.id.tvPriceCurrentItemEditMoneyDlg);
        itemEditDialog.setView(mViewItemEditDialog);

        btnOrder.setOnClickListener(this);
        btnCancelItemEditDlg.setOnClickListener(this);
        btnApproveItemEditDlg.setOnClickListener(this);
    }


    private void initAdapter() {
        itemInCartAdapter = new ItemInCartAdapter(getApplicationContext(), lstItemCart, new ItemInCartAdapter.OnChildItemInCartClickListener() {
            @Override
            public void onPlusClick(ItemInCart item) {
                setNumOrder(item);
                itemInCartAdapter.notifyDataSetChanged();
                calculatorCurrentTotalPrice();
            }

            @Override
            public void onMinusClick(ItemInCart item) {
                setNumOrder(item);
                itemInCartAdapter.notifyDataSetChanged();
                calculatorCurrentTotalPrice();
            }

            @Override
            public void onDeletedClick(ItemInCart item) {
                showDialogRemoveItem(item);
                itemInCartAdapter.notifyDataSetChanged();
                calculatorCurrentTotalPrice();
            }

            @Override
            public void onItemLongClick(ItemInCart item) {
                itemInCartEdit = item;
                setDataForEditItemDlg(item);
                ResourceUtils.showAlertDialog(itemEditDialog);
            }

        });
        rcvItems.setAdapter(itemInCartAdapter);
    }


    private void initPrinter() {
        netReceiver = new Receiver();
        registerReceiver(netReceiver, new IntentFilter(SettingFragment.DISCONNECT));
    }

    private void setDataForEditItemDlg(ItemInCart item) {
        edtPriceChangeItemEditDlg.setText("");
        tvNameItemEditDlg.setText(item.getItem().getName());
        tvUnitItemEditDlg.setText(item.getUnitChoose().getName());
        tvPriceCurrentItemEditDlg.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));

        edtPriceChangeItemEditDlg.setSelection(edtPriceChangeItemEditDlg.getText().length());
    }

    private void setNumOrder(ItemInCart item) {
        if (lstItemCart.size() > 1) {
            for (int i = 0; i < lstItemCart.size(); i++) {
                ItemInCart obj = lstItemCart.get(i);
                if (item.getId().equals(obj.getItem().getId())
                        && item.getUnitChoose().getMaUnit().equals(obj.getUnitChoose().getMaUnit())) {
                    lstItemCart.set(i, item);
                    preferences.putListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);
                    break;
                }
            }
        }
    }

    public void calculatorCurrentTotalPrice() {
        totalPrice = 0L;
        if (lstItemCart != null && !lstItemCart.isEmpty()) {
            for (ItemInCart obj : lstItemCart) {
                totalPrice += obj.getTotal();
            }
        }
        tvTotalPrice.setText(StringFormatUtils.convertToStringMoneyVND(totalPrice));
    }

    private void onClickButtonCancelEditMoneyDlg() {
        clearEditItemDialog();
        ResourceUtils.hiddenAlertDialog(itemEditDialog);
    }

    private void onClickButtonApproveEditMoneyDlg() {
        long newPrice;
        long priceInput = Long.parseLong(edtPriceChangeItemEditDlg.getText().toString().trim());
        if (priceInput == 0) {
            newPrice = itemInCartEdit.getPrice();
        } else if (priceInput < 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.vui_long_nhap_dung_thong_tin_gia_thay_doi), Toast.LENGTH_SHORT).show();
            return;
        } else {
            newPrice = ResourceUtils.calculateNewPriceForItem(itemInCartEdit.getPrice(), priceInput);
        }
        itemInCartEdit.setPrice(newPrice);
        itemInCartEdit.setTotal(itemInCartEdit.getQuantity() * newPrice);

        setNumOrder(itemInCartEdit);
        itemInCartAdapter.notifyDataSetChanged();
        calculatorCurrentTotalPrice();
        ResourceUtils.hiddenAlertDialog(itemEditDialog);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.cap_nhat_thanh_cong), Toast.LENGTH_SHORT).show();
    }

    private void showDialogRemoveItem(ItemInCart item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn muốn bỏ sản phẩm này?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            lstItemCart.remove(item);
            calculatorCurrentTotalPrice();
            preferences.putListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);
            itemInCartAdapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Hủy bỏ", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void clearEditItemDialog() {
        itemInCartEdit = null;
        tvNameItemEditDlg.setText("");
        tvUnitItemEditDlg.setText("");
        edtPriceChangeItemEditDlg.setText("0");
        tvPriceCurrentItemEditDlg.setText("0 đ");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOrderCart) {
            onClickButtonOrder();
        }
    }

    private void onClickButtonOrder() {
        if (SettingFragment.CONNECTED) {
            printBill();
        } else {
            showSnackBar(getString(R.string.can_ket_noi_may_in));
        }
    }

    private void printBill() {
        SettingFragment.binder.writeDataByYouself(
                new UiExecute() {
                    @Override
                    public void onsucess() {

                    }

                    @Override
                    public void onfailed() {

                    }
                }, () -> {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    byte[] arrBytes = StringFormatUtils.stringToBytes("Tran Ngoc Son");
                    list.add(arrBytes);
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1));
                    return list;
                });
    }


    private static class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SettingFragment.DISCONNECT)) {
                Message message = new Message();
                message.what = 4;
            }
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(getCurrentFocus(), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

}
