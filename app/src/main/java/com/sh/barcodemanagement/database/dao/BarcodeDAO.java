package com.sh.barcodemanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sh.barcodemanagement.database.entity.BarcodeEntity;
import com.sh.barcodemanagement.database.entity.ItemEntity;
import com.sh.barcodemanagement.database.entity.UnitEntity;

import java.util.List;

@Dao
public interface BarcodeDAO {

    @Query("SELECT * FROM item")
    List<ItemEntity> findAllItems();

    @Query("SELECT * FROM unit")
    List<UnitEntity> findAllUnits();

    @Query("SELECT * FROM barcode")
    List<BarcodeEntity> findAllBarCodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListItems(List<ItemEntity> itemEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListUnits(List<UnitEntity> unitEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListBarCodes(List<BarcodeEntity> barcodeEntities);

    @Query("SELECT * FROM item where barcode = :barcode")
    ItemEntity findItemEntityByBarcode(String barcode);

    @Query("SELECT a.* FROM item a INNER JOIN barcode b ON a.code = b.itemCode and b.barcode = :barcode")
    ItemEntity findItemEntityByBarcodeSub(String barcode);

}
