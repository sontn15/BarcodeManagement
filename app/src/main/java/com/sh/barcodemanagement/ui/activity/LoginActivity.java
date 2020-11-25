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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.network.BarcodeApiService;
import com.sh.barcodemanagement.network.RetrofitClient;
import com.sh.barcodemanagement.utils.Const;
import com.sh.barcodemanagement.utils.NetworkUtils;
import com.sh.barcodemanagement.utils.ResourceUtils;

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
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
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
