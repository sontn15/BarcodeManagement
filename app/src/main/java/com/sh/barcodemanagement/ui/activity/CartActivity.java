package com.sh.barcodemanagement.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.adapter.ItemInCartAdapter;
import com.sh.barcodemanagement.adapter.config.RecyclerItemTouchHelper;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.ui.fragment.ItemFragment;
import com.sh.barcodemanagement.ui.fragment.SettingFragment;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.ResourceUtils;
import com.sh.barcodemanagement.utils.StringFormatUtils;

import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private Button btnOrder;
    private TextView tvTotalPrice;
    private RecyclerView rcvItems;
    private List<ItemInCart> lstItemCart;
    private ProgressDialog progressDialog;
    private ItemInCartAdapter itemInCartAdapter;

    private View mViewEditDialog;
    private AlertDialog editDialog;
    private EditText edtPriceChangeDlg;
    private Button btnCancelDlg, btnApproveDlg;
    private TextView tvNameDlg, tvPriceCurrentDlg, tvUnitDlg;

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
        preferences = new MySharedPreferences(CartActivity.this);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvItems.setItemAnimator(new DefaultItemAnimator());
        rcvItems.setLayoutManager(layoutManager);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcvItems);

        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        //edit price dialog
        editDialog = new AlertDialog.Builder(CartActivity.this, R.style.CustomAlertDialog).create();
        mViewEditDialog = getLayoutInflater().inflate(R.layout.dialog_edit_item_in_cart, null);
        tvNameDlg = mViewEditDialog.findViewById(R.id.tvNameItemEditMoneyDlg);
        tvUnitDlg = mViewEditDialog.findViewById(R.id.tvUnitItemEditMoneyDlg);
        btnCancelDlg = mViewEditDialog.findViewById(R.id.btnCancelEditMoneyDlg);
        btnApproveDlg = mViewEditDialog.findViewById(R.id.btnApproveEditMoneyDlg);
        edtPriceChangeDlg = mViewEditDialog.findViewById(R.id.edtPriceChangeItemEditMoneyDlg);
        tvPriceCurrentDlg = mViewEditDialog.findViewById(R.id.tvPriceCurrentItemEditMoneyDlg);
        editDialog.setView(mViewEditDialog);

        btnOrder.setOnClickListener(this);
        btnCancelDlg.setOnClickListener(this);
        btnApproveDlg.setOnClickListener(this);
    }

    private void initPrinter() {
        netReceiver = new Receiver();
        registerReceiver(netReceiver, new IntentFilter(SettingFragment.DISCONNECT));
    }

    private void initAdapter() {
        itemInCartAdapter = new ItemInCartAdapter(CartActivity.this, lstItemCart, new ItemInCartAdapter.OnChildItemInCartClickListener() {
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
            public void onItemLongClick(ItemInCart item) {
                itemInCartEdit = item;
                edtPriceChangeDlg.setText("");
                tvNameDlg.setText(item.getItem().getName());
                tvUnitDlg.setText(item.getUnitChoose().getName());
                tvPriceCurrentDlg.setText(StringFormatUtils.convertToStringMoneyVND(item.getPrice()));
                edtPriceChangeDlg.setSelection(edtPriceChangeDlg.getText().length());
                ResourceUtils.showAlertDialog(editDialog);
            }
        });
        rcvItems.setAdapter(itemInCartAdapter);
    }

    private void setNumOrder(ItemInCart item) {
        if (lstItemCart.size() > 1) {
            for (int i = 0; i < lstItemCart.size(); i++) {
                ItemInCart obj = lstItemCart.get(i);
                if (item.getId().equals(obj.getItem().getId()) && item.getUnitChoose().getMaUnit().equals(obj.getUnitChoose().getMaUnit())) {
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

    private void clearEditItemDialog() {
        itemInCartEdit = null;
        tvNameDlg.setText("");
        tvUnitDlg.setText("");
        edtPriceChangeDlg.setText("0");
        tvPriceCurrentDlg.setText("0 đ");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOrderCart: {
                onClickButtonOrder();
                break;
            }
            case R.id.btnCancelEditMoneyDlg: {
                onClickButtonCancelEditDlg();
                break;
            }

            case R.id.btnApproveEditMoneyDlg: {
                onClickButtonApproveEditDlg();
                break;
            }
        }
    }

    private void onClickButtonOrder() {
        if (SettingFragment.CONNECTED) {
            printBill();
        } else {
            showSnackBar(getString(R.string.can_ket_noi_may_in));
        }
    }

    private void onClickButtonCancelEditDlg() {
        clearEditItemDialog();
        ResourceUtils.hiddenAlertDialog(editDialog);
    }

    private void onClickButtonApproveEditDlg() {
        long newPrice;
        long priceInput = Long.parseLong(edtPriceChangeDlg.getText().toString().trim());
        if (priceInput == 0) {
            newPrice = itemInCartEdit.getPrice();
        } else if (priceInput < 0) {
            Toast.makeText(CartActivity.this, getResources().getString(R.string.vui_long_nhap_dung_thong_tin_gia_thay_doi), Toast.LENGTH_SHORT).show();
            return;
        } else {
            newPrice = ResourceUtils.calculateNewPriceForItem(itemInCartEdit.getPrice(), priceInput);
        }
        itemInCartEdit.setPrice(newPrice);
        itemInCartEdit.setTotal(itemInCartEdit.getQuantity() * newPrice);

        setNumOrder(itemInCartEdit);
        itemInCartAdapter.notifyDataSetChanged();
        calculatorCurrentTotalPrice();
        ResourceUtils.hiddenAlertDialog(editDialog);
        Toast.makeText(CartActivity.this, getResources().getString(R.string.cap_nhat_thanh_cong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ItemInCartAdapter.ItemCartViewHolder) {
            ItemInCart deletedItem = lstItemCart.get(viewHolder.getAdapterPosition());
            lstItemCart.remove(deletedItem);
            calculatorCurrentTotalPrice();
            preferences.putListItemInCart(Const.KEY_SHARE_PREFERENCE.KEY_LIST_ITEM_CART, lstItemCart);
            itemInCartAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ItemFragment.calculatorCurrentTotalPrice(lstItemCart);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ResourceUtils.destroyAlertDialog(editDialog);
        ResourceUtils.destroyProgressDialog(progressDialog);
    }

    private void showSnackBar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

}
