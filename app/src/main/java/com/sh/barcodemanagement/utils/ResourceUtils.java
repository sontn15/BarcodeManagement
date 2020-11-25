package com.sh.barcodemanagement.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class ResourceUtils {

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

    /**
     * Lay ra he so co so cua san pham theo san pham dau vao
     */
    public static Long buildHeSoCoSoForItemInCart(Item itemClick) {
        Long heSoCoSo = 1L;
        Long unitDefault = itemClick.getUnitDefault();
        if (unitDefault != null && unitDefault != 0L) {
            if (unitDefault.equals(itemClick.getUnit1())) {
                heSoCoSo = itemClick.getQuyCach1();
            } else if (unitDefault.equals(itemClick.getUnit2())) {
                heSoCoSo = itemClick.getQuyCach2();
            } else {
                heSoCoSo = 1L;
            }
        }
        return heSoCoSo;
    }


    /**
     * Lay ra danh sach don vi tinh theo san pham dau vao
     */
    public static List<Unit> buildListUnitInItem(ItemInCart obj) {
        List<Unit> arrUnits = new ArrayList<>();
        if (obj.getItem().getUnitDefaultObj() != null) {
            arrUnits.add(obj.getItem().getUnitDefaultObj());
        }
        if (obj.getItem().getUnitMinObj() != null && !arrUnits.contains(obj.getItem().getUnitMinObj())) {
            arrUnits.add(obj.getItem().getUnitMinObj());
        }
        if (obj.getItem().getUnit1Obj() != null && !arrUnits.contains(obj.getItem().getUnit1Obj())) {
            arrUnits.add(obj.getItem().getUnit1Obj());
        }
        if (obj.getItem().getUnit2Obj() != null && !arrUnits.contains(obj.getItem().getUnit2Obj())) {
            arrUnits.add(obj.getItem().getUnit2Obj());
        }
        return arrUnits;
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
        }
    }

    public static void showAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
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
