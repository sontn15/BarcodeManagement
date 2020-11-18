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
public class Item implements Parcelable {

    private Long id;
    private String code;
    private String name;
    private Long giaNhap;
    private Long giaBanLe;

    private Long unitMin;
    private Unit unitMinObj;

    private boolean status;
    private Long storeId;

    private Long type;  //1. Kich thuoc, 2. Vat tu, 3. Dich vu, 4. Van tai

    private Long tonVien;

    private Long chotVien;

    private Long tNhapVien;

    private Long tXuatVien;


    protected Item(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        code = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            giaNhap = null;
        } else {
            giaNhap = in.readLong();
        }
        if (in.readByte() == 0) {
            giaBanLe = null;
        } else {
            giaBanLe = in.readLong();
        }
        if (in.readByte() == 0) {
            unitMin = null;
        } else {
            unitMin = in.readLong();
        }
        unitMinObj = in.readParcelable(Unit.class.getClassLoader());
        status = in.readByte() != 0;
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readLong();
        }
        if (in.readByte() == 0) {
            tonVien = null;
        } else {
            tonVien = in.readLong();
        }
        if (in.readByte() == 0) {
            chotVien = null;
        } else {
            chotVien = in.readLong();
        }
        if (in.readByte() == 0) {
            tNhapVien = null;
        } else {
            tNhapVien = in.readLong();
        }
        if (in.readByte() == 0) {
            tXuatVien = null;
        } else {
            tXuatVien = in.readLong();
        }
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
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
        dest.writeString(code);
        dest.writeString(name);
        if (giaNhap == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaNhap);
        }
        if (giaBanLe == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaBanLe);
        }
        if (unitMin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unitMin);
        }
        dest.writeParcelable(unitMinObj, flags);
        dest.writeByte((byte) (status ? 1 : 0));
        if (storeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storeId);
        }
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(type);
        }
        if (tonVien == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(tonVien);
        }
        if (chotVien == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(chotVien);
        }
        if (tNhapVien == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(tNhapVien);
        }
        if (tXuatVien == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(tXuatVien);
        }
    }
}
