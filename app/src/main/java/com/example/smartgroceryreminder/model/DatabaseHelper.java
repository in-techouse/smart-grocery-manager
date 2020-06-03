package com.example.smartgroceryreminder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public Context c;
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "products";
    public static final String COL1 = "ID";
    public static final String COL2 = "BRAND";
    public static final String COL3 = "NAME";
    public static final String COL4 = "IMAGE";
    public static final String COL5 = "MANUFACTURE_DATE";
    public static final String COL6 = "EXPIRY_DATE";
    public static final String COL7 = "ALARM";
    public static final String COL8 = "USAGE";
    public static final String COL9 = "EXPIRY_FORMATTED";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE " + TABLE_NAME + "( " +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                COL2 + " TEXT," +
                COL3 + " TEXT," +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " DATETIME, " +
                COL8 + " TEXT, " +
                COL9 + " DATE, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addData(GroceryItems groceryItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, groceryItems.getBrand());
        cv.put(COL3, groceryItems.getName());
        cv.put(COL4, groceryItems.getImage());
        cv.put(COL5, groceryItems.getManufactureDate());
        cv.put(COL6, groceryItems.getExpiryDate());
        cv.put(COL7, groceryItems.getAlarm());
        cv.put(COL8, groceryItems.getUseage());
        cv.put(COL9, groceryItems.getExpiryFormatted());

        return db.insert(TABLE_NAME, null, cv);
    }

    public List<GroceryItems> getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<GroceryItems> items = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY created_at DESC", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    GroceryItems item = new GroceryItems();
                    item.setId(cursor.getInt(0));
                    item.setBrand(cursor.getString(1));
                    item.setName(cursor.getString(2));
                    item.setImage(cursor.getString(3));
                    item.setManufactureDate(cursor.getString(4));
                    item.setExpiryDate(cursor.getString(5));
                    item.setAlarm(cursor.getString(6));
                    item.setUseage(cursor.getString(7));
                    item.setExpiryFormatted(cursor.getString(8));
                    items.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Exception: " + e.getMessage());
        }

        return items;
    }

    public List<GroceryItems> getItemsSorterByExpiry() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<GroceryItems> items = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL9 + " ASC", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    GroceryItems item = new GroceryItems();
                    item.setId(cursor.getInt(0));
                    item.setBrand(cursor.getString(1));
                    item.setName(cursor.getString(2));
                    item.setImage(cursor.getString(3));
                    item.setManufactureDate(cursor.getString(4));
                    item.setExpiryDate(cursor.getString(5));
                    item.setAlarm(cursor.getString(6));
                    item.setUseage(cursor.getString(7));
                    item.setExpiryFormatted(cursor.getString(8));
                    items.add(item);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("Database", "Exception: " + e.getMessage());
        }

        return items;
    }

    public void deleteItem(GroceryItems item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(TABLE_NAME, COL1 + "=" + item.getId(), null);
            Log.e("Database", "Delete Result: " + result);
        } catch (Exception e) {
            Log.e("Database", "Exception: " + e.getMessage());
        }
    }
}