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
public class Unit implements Parcelable {

    private Long id;
    private Long maUnit;
    private String name;
    private String note;
    private boolean status;
    private Long storeId;


    protected Unit(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            maUnit = null;
        } else {
            maUnit = in.readLong();
        }
        name = in.readString();
        note = in.readString();
        status = in.readByte() != 0;
        if (in.readByte() == 0) {
            storeId = null;
        } else {
            storeId = in.readLong();
        }
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        if (maUnit == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(maUnit);
        }
        parcel.writeString(name);
        parcel.writeString(note);
        parcel.writeByte((byte) (status ? 1 : 0));
        if (storeId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(storeId);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
