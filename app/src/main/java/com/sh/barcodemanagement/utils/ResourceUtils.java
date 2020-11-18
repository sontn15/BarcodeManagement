package com.sh.barcodemanagement.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.sh.barcodemanagement.model.Bill;

public class ResourceUtils {

    /**
     * Lay ra trang thai don hang theo don hang nhap vao
     */
    public static String getStatusOfBill(Bill bill) {
        String result;
        if (Const.STATUS_BILL_DESKTOP.CHUA_GUI.equals(bill.getStatusBillDesktop())) {
            result = Const.STATUS_BILL_DESKTOP.CHUA_GUI_STR;
        } else if (Const.STATUS_BILL_DESKTOP.DA_GUI.equals(bill.getStatusBillDesktop())) {
            result = Const.STATUS_BILL_DESKTOP.DA_GUI_STR;
        } else if (Const.STATUS_BILL_DESKTOP.DA_XU_LY.equals(bill.getStatusBillDesktop())) {
            result = Const.STATUS_BILL_DESKTOP.DA_XU_LY_STR;
        } else {
            result = Const.STATUS_BILL_DESKTOP.HUY_DON_HANG_STR;
        }
        return result;
    }

    public static Long calculateNewPriceForItem(Long oldPrice, Long newPrice) {
        if (oldPrice == null) {
            oldPrice = 0L;
        }
        if (newPrice == null) {
            newPrice = 0L;
        }
        Long calcPrice = newPrice;
        if (oldPrice.equals(0L) || newPrice.equals(0L)) {
            return newPrice;
        }
        if (oldPrice > newPrice) {
            long mMul = oldPrice / newPrice;
            if (mMul > 8) {
                calcPrice = calculateNewPriceForItem(oldPrice, newPrice * 10);
            }
        } else {
            long mMul = newPrice / oldPrice;
            if (mMul > 8) {
                calcPrice = calculateNewPriceForItem(oldPrice, newPrice / 10);
            }
        }
        return calcPrice;
    }

    public static void showProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void hiddenProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void destroyProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void hiddenAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static void destroyAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void showKeyboard(AlertDialog alertDialog) {
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void hiddenKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
