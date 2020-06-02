package com.example.smartgroceryreminder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                COL7 + " DATETIME )";
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
        cv.put(COL7, groceryItems.getExpiryDate());


        long result = db.insert(TABLE_NAME, null, cv);
        return result;
    }

//    public Cursor getListContents() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        return data;
//    }

//    public ArrayList<GroceryItems> fetchAllData() {
//        ArrayList<GroceryItems> groceryItems = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
////        db.update()
////        String s[] = new String[]{Title , Price};
//        //db.rawQuery();
//////        Cursor c = db.query(BooksTable , null , "email = ? AND password = ?" , new String[]{})
////        String col[] = new String[]{COL1 , Price};
//        /*************************Retrieve All Data**********************/
//
//        Cursor cursor = db.query(TABLE_NAME, null, null,
//                null, null, null, null);
//
//        if (cursor.getCount() == 0) {
//            Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
//        } else {
//            cursor.moveToFirst();
//            int i = 0;
//            while (i < cursor.getCount()) {
//                GroceryItems groceryItems1 = new GroceryItems();
//                int id = cursor.getInt(0);
//                groceryItems1.setId(id);
//                groceryItems1.setPname(cursor.getString(1));
//                groceryItems1.setManufacture(cursor.getString(2));
//                groceryItems1.setExpiry(cursor.getString(3));
//                groceryItems.add(groceryItems1);
//                cursor.moveToNext();
//                i++;
//            }
//        }
//
//        /*************************SEARCHING**********************/
////        Cursor cursor1 = db.query(BooksTable , new String[]{Title , Price} , Title+" = ? AND Price <= ?" , new String[]{"NAME" , "2000"} , null , null , null);
//////
////        Cursor cursor2 = db.query(BooksTable, new String[]{Title, BookId }, "Id > ? AND Id < ?", new String[]{"2" , "50"}, null, null, null);
////        cursor1.moveToFirst();
////        while (!cursor1.isLast()) {
////            Book book = new Book();
////            book.setId(cursor1.getInt(1));
////            book.setTitle(cursor1.getString(0));
////            booksList.add(book);
////            cursor1.moveToNext();
////        }
//        return groceryItems;
//    }
}