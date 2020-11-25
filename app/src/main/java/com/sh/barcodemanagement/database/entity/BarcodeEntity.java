package com.sh.barcodemanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "barcode")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "itemCode")
    private String itemCode;

    @ColumnInfo(name = "storeId")
    private Long storeId;
}
