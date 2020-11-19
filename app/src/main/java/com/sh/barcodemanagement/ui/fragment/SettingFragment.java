package com.sh.barcodemanagement.ui.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.sh.barcodemanagement.R;
import com.sh.barcodemanagement.database.MySharedPreferences;
import com.sh.barcodemanagement.utils.Const;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;

import static android.content.Context.BIND_AUTO_CREATE;

public class SettingFragment extends Fragment implements View.OnClickListener {

    public static String DISCONNECT = "com.sh.barcodemanagement.DISCONNECTED";

    private View mView;
    private Button btnConnect;
    private EditText edtIPAddress;
    private TextView tvInfoConnect;
    private TextView tvStatusConnect;
    private RadioButton rb58mm, rb80mm;

    public static boolean CONNECTED;
    public static IMyBinder binder;
    private MySharedPreferences preferences;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (IMyBinder) iBinder;
            Log.e(Const.LOG_TAG, "Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(Const.LOG_TAG, "Disconnected");
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        bindingPrinter();
        initView();
        initData();
        return mView;
    }

    private void initData() {
        preferences = new MySharedPreferences(requireActivity());
        String ipPrinter = preferences.getString(Const.KEY_SHARE_PREFERENCE.KEY_IP_PRINTER);
        if (ipPrinter != null && !ipPrinter.isEmpty()) {
            edtIPAddress.setText(ipPrinter);
        }
    }

    private void bindingPrinter() {
        Intent intent = new Intent(requireActivity(), PosprinterService.class);
        requireActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void initView() {
        rb58mm = mView.findViewById(R.id.rb58mm);
        rb80mm = mView.findViewById(R.id.rb80mm);
        edtIPAddress = mView.findViewById(R.id.edtIPSetting);
        btnConnect = mView.findViewById(R.id.btnConnectSetting);
        tvInfoConnect = mView.findViewById(R.id.tvInfoConnectSetting);
        tvStatusConnect = mView.findViewById(R.id.tvStatusConnectSetting);

        btnConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnectSetting: {
                onClickButtonConnect();
            }
        }
    }

    private void onClickButtonConnect() {
        String ipAddress = edtIPAddress.getText().toString().trim();
        if (ipAddress.equals("")) {
            showSnackBar(getString(R.string.nhap_ip_may_in));
        } else {
            if (!CONNECTED) {
                tvInfoConnect.setText("IP máy in: " + ipAddress);
                tvStatusConnect.setText("Đang kết nối, vui lòng đợi...");
                binder.connectNetPort(ipAddress, 9100, new UiExecute() {
                    @Override
                    public void onsucess() {
                        CONNECTED = true;
                        preferences.putString(Const.KEY_SHARE_PREFERENCE.KEY_IP_PRINTER, ipAddress);
                        tvStatusConnect.setText("Trạng thái: " + getString(R.string.ket_noi_thanh_cong));
                        tvStatusConnect.setTextColor(getResources().getColor(R.color.colorButtonGreen));
                        showSnackBar(getString(R.string.ket_noi_thanh_cong));

                        binder.acceptdatafromprinter(new UiExecute() {
                            @Override
                            public void onsucess() {

                            }

                            @Override
                            public void onfailed() {
                                CONNECTED = false;
                                showSnackBar(getString(R.string.ket_noi_that_bai));
                                Intent intent = new Intent();
                                intent.setAction(DISCONNECT);
                                requireActivity().sendBroadcast(intent);
                            }
                        });
                    }

                    @Override
                    public void onfailed() {
                        CONNECTED = false;
                        showSnackBar(getString(R.string.ket_noi_that_bai));
                        tvStatusConnect.setText("Trạng thái: " + getString(R.string.ket_noi_that_bai));
                        tvStatusConnect.setTextColor(getResources().getColor(R.color.colorVinID));
                    }
                });
            } else {
                showSnackBar(getString(R.string.da_ketnoi_truoc));
            }
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }
}
