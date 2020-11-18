package com.sh.barcodemanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements Parcelable {

    private Long id;
    private String customerName;
    private String customerAddress;
    private Long totalMoney;
    private String day;
    private Date createdDate;
    private String description;
    private boolean status;
    private Store store;
    private Long storeId;
    private Long statusBillDesktop;
    private List<SubBill> lstSubBills;


    /**
     * Fix date using Parcelable
     * createdDate = (java.util.Date) in.readSerializable();
     * dest.writeSerializable(createdDate);
     */
    protected Bill(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        customerName = in.readString();
        customerAddress = in.readString();
        if (in.readByte() == 0) {
            totalMoney = null;
        } else {
            totalMoney = in.readLong();
        }
        day = in.readString();
        createdDate = (Date) in.readSerializable();
        description = in.readString();
        status = in.readByte() != 0;
        store = in.readParcelable(Store.class.getClassLoader());
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
        }
        if (in.readByte() == 0) {
            statusBillDesktop = null;
        } else {
            statusBillDesktop = in.readLong();
        }
        lstSubBills = in.createTypedArrayList(SubBill.CREATOR);
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(customerName);
        dest.writeString(customerAddress);
        if (totalMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(totalMoney);
        }
        dest.writeString(day);
        dest.writeSerializable(createdDate);
        dest.writeString(description);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeParcelable(store, flags);
        if (storeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storeId);
        }
        if (statusBillDesktop == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(statusBillDesktop);
        }
        dest.writeTypedList(lstSubBills);
    }
}
