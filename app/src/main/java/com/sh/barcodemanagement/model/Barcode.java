package com.sh.barcodemanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Barcode implements Parcelable {

    private Long id;
    private Long storeId;
    private String itemCode;
    private String barcode;
    private Long quantity;
    private Long price;

    protected Barcode(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
        }
        itemCode = in.readString();
        barcode = in.readString();
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
    }

    public static final Creator<Barcode> CREATOR = new Creator<Barcode>() {
        @Override
        public Barcode createFromParcel(Parcel in) {
            return new Barcode(in);
        }

        @Override
        public Barcode[] newArray(int size) {
            return new Barcode[size];
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
        if (storeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storeId);
        }
        dest.writeString(itemCode);
        dest.writeString(barcode);
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
    }
}
