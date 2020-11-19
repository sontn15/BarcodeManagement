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
public class ItemInCart implements Parcelable {
    private Long id;
    private Item item;
    private Long subBillId;
    private Unit unitChoose;
    private Long quantity = 0L;
    private Long price = 0L;
    private Long total = 0L;
    private Long unitCoSo;
    private Long heSoCoSo;


    protected ItemInCart(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        item = in.readParcelable(Item.class.getClassLoader());
        if (in.readByte() == 0) {
            subBillId = null;
        } else {
            subBillId = in.readLong();
        }
        unitChoose = in.readParcelable(Unit.class.getClassLoader());
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
            total = null;
        } else {
            total = in.readLong();
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

    public static final Creator<ItemInCart> CREATOR = new Creator<ItemInCart>() {
        @Override
        public ItemInCart createFromParcel(Parcel in) {
            return new ItemInCart(in);
        }

        @Override
        public ItemInCart[] newArray(int size) {
            return new ItemInCart[size];
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
        if (subBillId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(subBillId);
        }
        dest.writeParcelable(unitChoose, flags);
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
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(total);
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
