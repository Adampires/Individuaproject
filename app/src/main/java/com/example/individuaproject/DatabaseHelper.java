package com.example.individuaproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ElectricityBills.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bills";

    // Column names
    private static final String COL_ID = "id";
    private static final String COL_MONTH = "month";
    private static final String COL_YEAR = "year";
    private static final String COL_KWH = "kwh";
    private static final String COL_TOTAL = "total";
    private static final String COL_REBATE = "rebate";
    private static final String COL_FINAL = "final";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONTH + " TEXT NOT NULL, " +
                COL_YEAR + " TEXT NOT NULL, " +
                COL_KWH + " REAL NOT NULL, " +
                COL_TOTAL + " REAL NOT NULL, " +
                COL_REBATE + " REAL NOT NULL, " +
                COL_FINAL + " REAL NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new bill record
    public boolean insertBill(String month, String year, double kwh, double total, double rebate, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_MONTH, month);
        values.put(COL_YEAR, year);
        values.put(COL_KWH, kwh);
        values.put(COL_TOTAL, total);
        values.put(COL_REBATE, rebate);
        values.put(COL_FINAL, finalCost);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // returns true if insert successful
    }

    // Get all saved bills
    public Cursor getAllBills() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC", null);
    }

    // Get a single bill by its ID
    public Cursor getOneData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a bill by ID
    public boolean deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
