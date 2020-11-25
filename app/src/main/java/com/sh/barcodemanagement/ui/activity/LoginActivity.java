package com.sh.barcodemanagement.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.database.BarcodeDatabase;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.database.entity.BarcodeEntity;
import com.sh.barcodemanagement.database.entity.ItemEntity;
import com.sh.barcodemanagement.database.entity.UnitEntity;
import com.sh.barcodemanagement.model.Barcode;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.network.BarcodeApiService;
import com.sh.barcodemanagement.network.RetrofitClient;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.NetworkUtils;
import com.sh.barcodemanagement.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private EditText edtUsername, edtPassword;

    private String phoneInformation;
    private MySharedPreferences preferences;
    private BarcodeApiService barcodeApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkUserInSharePreference();
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ResourceUtils.destroyProgressDialog(progressDialog);
    }


    @SuppressLint("HardwareIds")
    private void initData() {
        phoneInformation = "[" + Build.ID + "||" + Build.MANUFACTURER + "||" + Build.MODEL + "||" + Build.PRODUCT + "]";
    }

    private void checkUserInSharePreference() {
        if (preferences == null) {
            preferences = new MySharedPreferences(LoginActivity.this);
        }
        if (barcodeApiService == null) {
            barcodeApiService = RetrofitClient.getClient().create(BarcodeApiService.class);
        }
        Store storeCurrent = preferences.getStore(Const.KEY_SHARE_PREFERENCE.KEY_STORE);
        if (storeCurrent != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initView() {
        edtUsername = this.findViewById(R.id.edt_uername_login);
        edtPassword = this.findViewById(R.id.edt_password_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.vui_long_doi) + "...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin = this.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            showSnackBar(getResources().getString(R.string.vui_long_nhap_day_du_thong_tin));
            return;
        }
        checkUserLogin(username, password);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void checkUserLogin(String username, String password) {
        if (NetworkUtils.haveNetwork(LoginActivity.this)) {
            ResourceUtils.showProgressDialog(progressDialog);
            Call<Store> callLogin = barcodeApiService.login(username.trim(), password.trim(), phoneInformation);
            callLogin.enqueue(new Callback<Store>() {
                @Override
                public void onResponse(Call<Store> call, Response<Store> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Store storeLogin = response.body();
                        preferences.putStore(Const.KEY_SHARE_PREFERENCE.KEY_STORE, storeLogin);

                        Call<List<Unit>> callUnits = barcodeApiService.findAllUnits(storeLogin.getId());
                        callUnits.enqueue(new Callback<List<Unit>>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<UnitEntity> entityList = new ArrayList<>();
                                    List<Unit> listUnits = response.body();
                                    listUnits.forEach(unit -> entityList.add(UnitEntity.builder()
                                            .id(unit.getId())
                                            .name(unit.getName())
                                            .note(unit.getNote())
                                            .storeId(unit.getStoreId())
                                            .build()));
                                    BarcodeDatabase.getInstance(LoginActivity.this).barcodeDAO().insertListUnits(entityList);

                                    Call<List<Barcode>> callBarCodes = barcodeApiService.findAllBarCodes(storeLogin.getId());
                                    callBarCodes.enqueue(new Callback<List<Barcode>>() {
                                        @Override
                                        public void onResponse(Call<List<Barcode>> call, Response<List<Barcode>> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                List<BarcodeEntity> barcodeEntities = new ArrayList<>();
                                                List<Barcode> listBarCodes = response.body();
                                                listBarCodes.forEach(barcode -> barcodeEntities.add(BarcodeEntity.builder()
                                                        .id(barcode.getId())
                                                        .itemCode(barcode.getItemCode())
                                                        .barcode(barcode.getBarcode())
                                                        .storeId(barcode.getStoreId())
                                                        .build()));
                                                BarcodeDatabase.getInstance(LoginActivity.this).barcodeDAO().insertListBarCodes(barcodeEntities);


                                                Call<List<Item>> callItems = barcodeApiService.getAllItems(storeLogin.getId());
                                                callItems.enqueue(new Callback<List<Item>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            List<ItemEntity> itemEntities = new ArrayList<>();
                                                            List<Item> listItemServer = response.body();
                                                            listItemServer.forEach(item -> itemEntities.add(ItemEntity.builder()
                                                                    .id(item.getId())
                                                                    .code(item.getCode())
                                                                    .name(item.getName())
                                                                    .giaBan(item.getGiaBan())
                                                                    .barcode(item.getBarcode())
                                                                    .unitMin(item.getUnitMin())
                                                                    .unitDefault(item.getUnitDefault())
                                                                    .unit1(item.getUnit1())
                                                                    .quyCach1(item.getQuyCach1())
                                                                    .giaQuyDoi1(item.getGiaQuyDoi1())
                                                                    .unit2(item.getUnit2())
                                                                    .quyCach2(item.getQuyCach2())
                                                                    .giaQuyDoi2(item.getGiaQuyDoi2())
                                                                    .build()));
                                                            BarcodeDatabase.getInstance(LoginActivity.this).barcodeDAO().insertListItems(itemEntities);

                                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Item>> call, Throwable t) {
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Barcode>> call, Throwable t) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Unit>> call, Throwable t) {

                            }
                        });
                    } else {
                        showSnackBar(getResources().getString(R.string.taikhoan_matkhau_khong_dung));
                    }
                    ResourceUtils.showProgressDialog(progressDialog);
                }

                @Override
                public void onFailure(Call<Store> call, Throwable t) {
                    showSnackBar(getResources().getString(R.string.dang_nhap_thai_bai));
                    ResourceUtils.showProgressDialog(progressDialog);
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.check_connection_network));
            ResourceUtils.showProgressDialog(progressDialog);
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(getCurrentFocus(), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

}
