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
    private Long giaBuon;
    private Long giaDaiLy;

    private Long unitMin;
    private Unit unitMinObj;

    private Long unitDefault;
    private Unit unitDefaultObj;

    private Long unit1;
    private Unit unit1Obj;
    private Long quyCach1;
    private Long giaQuyDoi1;

    private Long storeId;
    private boolean status;
    private String barcode;


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
            giaBuon = null;
        } else {
            giaBuon = in.readLong();
        }
        if (in.readByte() == 0) {
            giaDaiLy = null;
        } else {
            giaDaiLy = in.readLong();
        }
        if (in.readByte() == 0) {
            unitMin = null;
        } else {
            unitMin = in.readLong();
        }
        unitMinObj = in.readParcelable(Unit.class.getClassLoader());
        if (in.readByte() == 0) {
            unitDefault = null;
        } else {
            unitDefault = in.readLong();
        }
        unitDefaultObj = in.readParcelable(Unit.class.getClassLoader());
        if (in.readByte() == 0) {
            unit1 = null;
        } else {
            unit1 = in.readLong();
        }
        unit1Obj = in.readParcelable(Unit.class.getClassLoader());
        if (in.readByte() == 0) {
            quyCach1 = null;
        } else {
            quyCach1 = in.readLong();
        }
        if (in.readByte() == 0) {
            giaQuyDoi1 = null;
        } else {
            giaQuyDoi1 = in.readLong();
        }
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
        }
        status = in.readByte() != 0;
        barcode = in.readString();
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
        if (giaBuon == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaBuon);
        }
        if (giaDaiLy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaDaiLy);
        }
        if (unitMin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unitMin);
        }
        dest.writeParcelable(unitMinObj, flags);
        if (unitDefault == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unitDefault);
        }
        dest.writeParcelable(unitDefaultObj, flags);
        if (unit1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unit1);
        }
        dest.writeParcelable(unit1Obj, flags);
        if (quyCach1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(quyCach1);
        }
        if (giaQuyDoi1 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaQuyDoi1);
        }
        if (storeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(storeId);
        }
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(barcode);
    }
}
