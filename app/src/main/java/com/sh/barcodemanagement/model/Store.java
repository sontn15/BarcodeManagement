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
public class Store implements Parcelable {

    private Long id;
    private String code;
    private String name;
    private Long type;
    private String note;
    private String address;
    private String username;
    private String password;
    private Long quantityDevice;
    private String serialDevice;

    protected Store(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        code = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readLong();
        }
        note = in.readString();
        address = in.readString();
        username = in.readString();
        password = in.readString();
        if (in.readByte() == 0) {
            quantityDevice = null;
        } else {
            quantityDevice = in.readLong();
        }
        serialDevice = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
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
        if (type == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(type);
        }
        dest.writeString(note);
        dest.writeString(address);
        dest.writeString(username);
        dest.writeString(password);
        if (quantityDevice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(quantityDevice);
        }
        dest.writeString(serialDevice);
    }
}
