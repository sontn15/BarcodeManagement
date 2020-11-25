package com.sh.barcodemanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BarcodeDatabase";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + Const.TABLE_UNIT.TABLE_NAME + "("
                + Const.TABLE_UNIT.KEY_ID + " LONG PRIMARY KEY,"
                + Const.TABLE_UNIT.KEY_NAME + " TEXT,"
                + Const.TABLE_UNIT.KEY_STORE_ID + " LONG" + ")";
        db.execSQL(CREATE_COUNTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const.TABLE_UNIT.TABLE_NAME);
        onCreate(db);
    }

    public List<Unit> getAllUnits() {
        List<Unit> data = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Const.TABLE_UNIT.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(Unit.builder()
                        .id(cursor.getLong(0))
                        .name(cursor.getString(1))
                        .storeId(cursor.getLong(2))
                        .build());
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void insertUnits(List<Unit> listUnit) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Unit unit : listUnit) {
            ContentValues values = new ContentValues();
            values.put(Const.TABLE_UNIT.KEY_ID, unit.getId());
            values.put(Const.TABLE_UNIT.KEY_NAME, unit.getName());
            values.put(Const.TABLE_UNIT.KEY_STORE_ID, unit.getStoreId());

            db.insert(Const.TABLE_UNIT.TABLE_NAME, null, values);
        }
        db.close();
    }

    public Unit getUnitById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Const.TABLE_UNIT.TABLE_NAME,
                new String[]{Const.TABLE_UNIT.KEY_ID, Const.TABLE_UNIT.KEY_NAME, Const.TABLE_UNIT.KEY_STORE_ID},
                Const.TABLE_UNIT.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return Unit.builder()
                .id(cursor.getLong(0))
                .name(cursor.getString(1))
                .storeId(cursor.getLong(2))
                .build();
    }

    public void deleteAllUnits() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Const.TABLE_UNIT.TABLE_NAME, null, null);
        db.close();
    }

}