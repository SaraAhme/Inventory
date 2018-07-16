package com.example.android.inventory.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.data.flowerContract.flowerEntry;

public class flowerDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = flowerDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "Bouquets.db";
    private static final int DATABASE_VERSION = 1;
    public flowerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + flowerEntry.TABLE_NAME + " ("
                + flowerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + flowerEntry.COLUMN_FLOWER_NAME+ " TEXT NOT NULL, "
                + flowerEntry.COLUMN_FLOWER_PRICE + " INTEGER NOT NULL, "
                + flowerEntry.COLUMN_FLOWER_QUANTITY + " INTEGER NOT NULL, "
                + flowerEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL, "
                + flowerEntry.COLUMN_FLOWER_SUPPLIER + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}