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
    private Long giaBan;
    private String barcode;

    private Long unitMin;
    private Unit unitMinObj;

    private Long unitDefault;
    private Unit unitDefaultObj;

    private Long unit1;
    private Unit unit1Obj;
    private Long quyCach1;
    private Long giaQuyDoi1;

    private Long unit2;
    private Unit unit2Obj;
    private Long quyCach2;
    private Long giaQuyDoi2;

    private boolean status;

    protected Item(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        code = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            giaBan = null;
        } else {
            giaBan = in.readLong();
        }
        barcode = in.readString();
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
            unit2 = null;
        } else {
            unit2 = in.readLong();
        }
        unit2Obj = in.readParcelable(Unit.class.getClassLoader());
        if (in.readByte() == 0) {
            quyCach2 = null;
        } else {
            quyCach2 = in.readLong();
        }
        if (in.readByte() == 0) {
            giaQuyDoi2 = null;
        } else {
            giaQuyDoi2 = in.readLong();
        }
        status = in.readByte() != 0;
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
        if (giaBan == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaBan);
        }
        dest.writeString(barcode);
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
        if (unit2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unit2);
        }
        dest.writeParcelable(unit2Obj, flags);
        if (quyCach2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(quyCach2);
        }
        if (giaQuyDoi2 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(giaQuyDoi2);
        }
        dest.writeByte((byte) (status ? 1 : 0));
    }
}
