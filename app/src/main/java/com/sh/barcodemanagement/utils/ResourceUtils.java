package com.sh.barcodemanagement.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;

import com.sh.barcodemanagement.model.Bill;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     * Lay ra he so co so cua san pham theo san pham dau vao
     */
    public static Long buildHeSoCoSoForItemInCart(Item itemClick) {
        Long heSoCoSo = 1L;
        Long unitDefault = itemClick.getUnitDefault();
        if (unitDefault != null && unitDefault != 0L) {
            if (unitDefault.equals(itemClick.getUnit1())) {
                heSoCoSo = itemClick.getQuyCach1();
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
        return arrUnits;
    }

    /**
     * Lay ra gia quy doi mac dinh cua san pham
     */
    public static Long getPriceDefaultOfItemForCustomer(Item item) {
        Long unitDefault = 0L;
        if (item.getUnitDefault() != null) {
            unitDefault = item.getUnitDefault();
        }
        if (item.getUnitMin() != null && unitDefault.equals(item.getUnitMin())) {
            return item.getGiaBanLe();
        }
        if (item.getUnit1() != null && unitDefault.equals(item.getUnit1())) {
            return item.getGiaQuyDoi1();
        }
        return 0L;
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

    public static List<Item> buildListItems() {
        List<Item> data = new ArrayList<>();
        List<String> listNameItems = new ArrayList<>();
        listNameItems.add("Bánh qui Hải Châu (500gr)");
        listNameItems.add("Bánh qui Hải Châu (250gr)");
        listNameItems.add("Bánh qui Hải Châu (1000gr)");
        listNameItems.add("Thạch rau câu Long Hải");
        listNameItems.add("Thạch rau câu Nam Ngãi");
        listNameItems.add("Thạch rau câu Bà Tùy");
        listNameItems.add("Bánh gạo ăn tốt cho sức khỏe, trẻ đẹp, đẻ con tốt");
        listNameItems.add("Bánh kem");
        listNameItems.add("Bánh sữa Ba Vì, Vĩnh Phúc, Thành Phố Hà Nội");
        listNameItems.add("Bánh Vinamilk");
        listNameItems.add("Kẹo dừa Tam Hiệp, Vĩnh Phúc, Hà Đông, Hà Nội");
        listNameItems.add("Kẹo sữa dừa ngọt bùi");
        listNameItems.add("Kẹo cam vị tươi mát, ngọt bùi (1 kg)");
        listNameItems.add("Kẹo chanh");
        listNameItems.add("Kẹo lạc Nam Định, loại 1 không bán đắt");
        listNameItems.add("Kẹo vừng Hà Giang, nơi mảnh đất màu mỡ, cảnh đẹp hùng vĩ");
        listNameItems.add("Kẹo Osi giá quá rẻ với một loại kẹo");

        for (int i = 1; i < 2000; i++) {
            String code = "SP" + i;
            Long id = (long) i;
            Random random = new Random();
            Random random2 = new Random();
            Long giaBanLe = (long) random2.nextInt(1000000);
            int anInt = random.nextInt(listNameItems.size());
            String name = listNameItems.get(anInt);
            String barcode = String.valueOf(i);

            Item item = Item.builder()
                    .id(id).code(code).name(name).status(true).giaBanLe(giaBanLe).barcode(barcode)
                    .unitMin(id).unitMinObj(Unit.builder().id(id).maUnit(id).name("Gói").status(true).storeId(id).build())
                    .unitDefault(id).unitDefaultObj(Unit.builder().id(id).maUnit(id).name("Gói").status(true).storeId(id).build())
                    .build();
            data.add(item);
        }
        return data;
    }

}
