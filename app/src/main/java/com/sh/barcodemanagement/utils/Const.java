package com.sh.barcodemanagement.utils;

import com.sh.barcodemanagement.model.Unit;

public class Const {

    public static final String LOG_TAG = "SONTN7";
    public static final String KEY_IS_VIEW_BILL_DETAIL = "keyIsViewBillDetail";

    public static class KEY_SHARE_PREFERENCE {
        public static final String KEY_STORE = "keyStore";
        public static final String KEY_IP_PRINTER = "keyIPPrinter";
        public static final String KEY_MY_EMPLOYEE = "employeeLogin";
        public static final String KEY_BILL_CREATED = "keyBillCreated";
        public static final String KEY_LIST_CUSTOMER = "keyListCustomer";
        public static final String KEY_LIST_ITEM_CART = "keyListItemsCart";
        public static final String KEY_IS_BILL_CREATED = "keyIsBillCreated";
        public static final String KEY_LIST_CATEGORIES = "keyListCategories";
        public static final String KEY_LIST_ITEM_BY_CATEGORY = "keyListItemByCategory";
        public static final String KEY_LIST_ITEM_ALL = "keyListItemAll";
        public static final String KEY_CLICK_BUTTON_EDIT_BILL = "keyClickButtonEditBill";

        public static final String KEY_BILL_DETAIL = "billDetail";
        public static final String KEY_TOKEN_CURRENT = "currentToken";
        public static final String KEY_CHECK_CLICK_NOTIFICATION = "checkClickNotification";
    }

    public static class STATUS_BILL_DESKTOP {
        public static final Long CHUA_GUI = 0L;
        public static final String CHUA_GUI_STR = "Chưa gửi đơn hàng";

        public static final Long DA_GUI = 1L;
        public static final String DA_GUI_STR = "Đã gửi đơn hàng";

        public static final Long DA_XU_LY = 2L;
        public static final String DA_XU_LY_STR = "Đã xử lý đơn hàng";

        public static final Long HUY_DON_HANG = 4L;
        public static final String HUY_DON_HANG_STR = "Đã hủy đơn hàng";
    }

    public static class TABLE_ITEM {
        public static final String TABLE_NAME = "item";
        public static final String KEY_ID = "id";
        public static final String KEY_CODE = "code";
        public static final String KEY_NAME = "name";
        public static final String KEY_GIA_BAN_LE = "giaBanLe";
        public static final String KEY_GIA_BUON = "giaBuon";
        public static final String KEY_GIA_DAI_LY = "giaDaiLy";
        public static final String KEY_UNIT_MIN = "unitMin";
        public static final String KEY_UNIT_DEFAULT = "unitDefault";
        public static final String KEY_UNIT_1 = "unit1";
        public static final String KEY_QUY_CACH_1 = "quyCach1";
        public static final String KEY_GIA_QUY_DOI_1 = "giaQuyDoi1";
        public static final String KEY_STORE_ID = "storeId";
        public static final String KEY_BARCODE = "barcode";
    }

    public static class TABLE_UNIT {
        public static final String TABLE_NAME = "unit";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_STORE_ID = "storeId";
    }

    public static class REQUEST_CODE {
        public static final int REQUEST_CODE_SCANNER = 10;
    }

}
