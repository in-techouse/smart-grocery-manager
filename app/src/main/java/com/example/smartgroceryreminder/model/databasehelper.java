package com.example.smartgroceryreminder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class databasehelper extends SQLiteOpenHelper {

    public Context c;

    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "user_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "PRODUCTNAME";
    public static final String COL3 = "MANUFACUTREDATE";
    public static final String COL4 = "EXPIRYDATE";

    public databasehelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " + COL2 + " TEXT," + COL3 + " TEXT,"
                + COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(GroceryItems groceryItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, groceryItems.getPname());
        cv.put(COL3, groceryItems.getManufacture());
        cv.put(COL4, groceryItems.getExpiry());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(c, "Data Not Insert", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(c, "Data Inserted", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public ArrayList<GroceryItems> fetchAllData() {
        ArrayList<GroceryItems> groceryItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

//        db.update()
//        String s[] = new String[]{Title , Price};
        //db.rawQuery();
////        Cursor c = db.query(BooksTable , null , "email = ? AND password = ?" , new String[]{})
//        String col[] = new String[]{COL1 , Price};
        /*************************Retrieve All Data**********************/

        Cursor cursor = db.query(TABLE_NAME, null, null,
                null, null, null, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            int i = 0;
            while (i < cursor.getCount()) {
                GroceryItems groceryItems1 = new GroceryItems();
                int id = cursor.getInt(0);
                groceryItems1.setId(id);
                groceryItems1.setPname(cursor.getString(1));
                groceryItems1.setManufacture(cursor.getString(2));
                groceryItems1.setExpiry(cursor.getString(3));
                groceryItems.add(groceryItems1);
                cursor.moveToNext();
                i++;
            }
        }

        /*************************SEARCHING**********************/
//        Cursor cursor1 = db.query(BooksTable , new String[]{Title , Price} , Title+" = ? AND Price <= ?" , new String[]{"NAME" , "2000"} , null , null , null);
////
//        Cursor cursor2 = db.query(BooksTable, new String[]{Title, BookId }, "Id > ? AND Id < ?", new String[]{"2" , "50"}, null, null, null);
//        cursor1.moveToFirst();
//        while (!cursor1.isLast()) {
//            Book book = new Book();
//            book.setId(cursor1.getInt(1));
//            book.setTitle(cursor1.getString(0));
//            booksList.add(book);
//            cursor1.moveToNext();
//        }
        return groceryItems;
    }


}