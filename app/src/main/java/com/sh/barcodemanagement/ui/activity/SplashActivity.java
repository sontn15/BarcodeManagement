package com.sh.barcodemanagement.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.barcodemanagement.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
