package com.sh.barcodemanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "giaBan")
    private Long giaBan;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "unitMin")
    private Long unitMin;

    @ColumnInfo(name = "unitDefault")
    private Long unitDefault;

    @ColumnInfo(name = "unit1")
    private Long unit1;

    @ColumnInfo(name = "quyCach1")
    private Long quyCach1;

    @ColumnInfo(name = "giaQuyDoi1")
    private Long giaQuyDoi1;

    @ColumnInfo(name = "unit2")
    private Long unit2;

    @ColumnInfo(name = "quyCach2")
    private Long quyCach2;

    @ColumnInfo(name = "giaQuyDoi2")
    private Long giaQuyDoi2;

}
