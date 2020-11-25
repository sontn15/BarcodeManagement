package com.sh.barcodemanagement.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "unit")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "storeId")
    private Long storeId;

}
