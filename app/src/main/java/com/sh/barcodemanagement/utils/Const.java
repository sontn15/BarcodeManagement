package com.sh.barcodemanagement.utils;

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

    public static class PARAMETER {
        public static final String ID = "id";
        public static final String BILL_ID = "billId";
        public static final String COST_MONEY = "costMoney";
        public static final String CREATE_DATE = "createDate";
        public static final String BILL_NUMBER = "billNumber";
        public static final String TOTAL_MONEY = "totalMoney";
        public static final String DESCRIPTION = "description";
        public static final String TOTAL_GIA_VON = "totalGiaVon";
        public static final String INCOME_MONEY = "incomeMoney";
        public static final String CUSTOMER_NAME = "customerName";
        public static final String CUSTOMER_CODE = "customerCode";
        public static final String TYPE_NOTIFICATION = "typeNotification";
    }

    public static class NOTIFICATION {
        public static final String CHANNEL_NAME = "FCM DMartStone";
        public static final String CHANNEL_DESC = "Firebase Cloud Messaging DMartStone";
        public static final String NOTIFICATION_CHANNEL_ID = "com.sh.dmartstonemanagement";
    }

    public static class UNIT_TYPE_FOR_STORE {
        public static final Long CENTIMETRE = 1L;
        public static final Long MILLIMETRE = 2L;
    }

    public static class LOAI_SANPHAM {
        public static final Long KICH_THUOC = 1L;
        public static final Long VAT_TU = 2L;
        public static final Long DICH_VU = 3L;
        public static final Long VAN_TAI = 4L;
    }

    public static class EMPLOYEE_TYPE {
        public static final Long NHAN_VIEN = 1L;
        public static final Long QUAN_LY = 2L;
    }

}
