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
    private Long quantity = 0L;
    private Long price = 0L;
    private Long totalMoney = 0L;
    private Long unitCoSo;
    private Long heSoCoSo;

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
            quantity = in.readLong();
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
            unitCoSo = null;
        } else {
            unitCoSo = in.readLong();
        }
        if (in.readByte() == 0) {
            heSoCoSo = null;
        } else {
            heSoCoSo = in.readLong();
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
            dest.writeLong(quantity);
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
        if (unitCoSo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unitCoSo);
        }
        if (heSoCoSo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(heSoCoSo);
        }
    }
}
