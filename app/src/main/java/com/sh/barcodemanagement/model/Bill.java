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
    private Long totalMoney;
    private String day;
    private Date createdDate;
    private boolean status;
    private Long storeId;
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
        if (in.readByte() == 0) {
            totalMoney = null;
        } else {
            totalMoney = in.readLong();
        }
        day = in.readString();
        createdDate = (Date) in.readSerializable();
        status = in.readByte() != 0;
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
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
        if (totalMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(totalMoney);
        }
        dest.writeString(day);
        dest.writeSerializable(createdDate);
        dest.writeByte((byte) (status ? 1 : 0));
        if (storeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storeId);
        }
        dest.writeTypedList(lstSubBills);
    }
}
