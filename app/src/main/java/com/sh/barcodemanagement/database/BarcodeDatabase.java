package com.sh.barcodemanagement.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sh.barcodemanagement.database.dao.BarcodeDAO;
import com.sh.barcodemanagement.database.entity.BarcodeEntity;
import com.sh.barcodemanagement.database.entity.ItemEntity;
import com.sh.barcodemanagement.database.entity.UnitEntity;


@Database(entities = {ItemEntity.class, UnitEntity.class, BarcodeEntity.class}, version = 3)
public abstract class BarcodeDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "taphoa.db";
    private static BarcodeDatabase instance;

    public static synchronized BarcodeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), BarcodeDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract BarcodeDAO barcodeDAO();

}