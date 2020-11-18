package com.sh.barcodemanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubBill implements Parcelable {

    private Long id;
    private Item item;
    private String itemName;
    private Unit unit;
    private Double quantity = 0D;
    private Long price = 0L;
    private Long totalMoney = 0L;
    private Long d1Nut = 0L;
    private Long d2Nut = 0L;
    private Double dienTichNut = 0D;
    private Long chieuDai = 0L;
    private Long chieuRong = 0L;
    private Long itemType;
    private Long soLuongVien = 0L;


    protected SubBill(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        item = in.readParcelable(Item.class.getClassLoader());
        itemName = in.readString();
        unit = in.readParcelable(Unit.class.getClassLoader());
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readDouble();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readLong();
        }
        if (in.readByte() == 0) {
            totalMoney = null;
        } else {
            totalMoney = in.readLong();
        }
        if (in.readByte() == 0) {
            d1Nut = null;
        } else {
            d1Nut = in.readLong();
        }
        if (in.readByte() == 0) {
            d2Nut = null;
        } else {
            d2Nut = in.readLong();
        }
        if (in.readByte() == 0) {
            dienTichNut = null;
        } else {
            dienTichNut = in.readDouble();
        }
        if (in.readByte() == 0) {
            chieuDai = null;
        } else {
            chieuDai = in.readLong();
        }
        if (in.readByte() == 0) {
            chieuRong = null;
        } else {
            chieuRong = in.readLong();
        }
        if (in.readByte() == 0) {
            itemType = null;
        } else {
            itemType = in.readLong();
        }
        if (in.readByte() == 0) {
            soLuongVien = null;
        } else {
            soLuongVien = in.readLong();
        }
    }

    public static final Creator<SubBill> CREATOR = new Creator<SubBill>() {
        @Override
        public SubBill createFromParcel(Parcel in) {
            return new SubBill(in);
        }

        @Override
        public SubBill[] newArray(int size) {
            return new SubBill[size];
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
        dest.writeParcelable(item, flags);
        dest.writeString(itemName);
        dest.writeParcelable(unit, flags);
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(quantity);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(price);
        }
        if (totalMoney == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(totalMoney);
        }
        if (d1Nut == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(d1Nut);
        }
        if (d2Nut == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(d2Nut);
        }
        if (dienTichNut == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(dienTichNut);
        }
        if (chieuDai == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(chieuDai);
        }
        if (chieuRong == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(chieuRong);
        }
        if (itemType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(itemType);
        }
        if (soLuongVien == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(soLuongVien);
        }
    }
}
