package com.example.laptop.inventoryfinalapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by LAPTOP on 19-06-2017.
 */

public class ProductDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Producter.db";
    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_QUERY = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME
                + "(" + ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductContract.ProductEntry.NAME_OF_PRODUCT + " TEXT NOT NULL, "
                + ProductContract.ProductEntry.QUANTITY_OF_PRODUCT + " INTEGER NOT NULL, "
                + ProductContract.ProductEntry.COST_OF_PRODUCT + " INTEGER NOT NULL, "
                + ProductContract.ProductEntry.Image + " BLOB);";

        db.execSQL(SQL_QUERY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



