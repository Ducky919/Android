package com.hfad.myicon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class CalculatorDatabaseHelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "calculatorResult";
  public static final int DB_VERSION = 2;
  public static final String TABLE_NAME = "RESULT";
  public static final String COL_HISTORY_TEXT = "HISTORY_TEXT";

  CalculatorDatabaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "HISTORY_TEXT TEXT);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

  public void insertData(String numberResult) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_HISTORY_TEXT, numberResult);
    db.insert(TABLE_NAME, null, contentValues);
  }

  public ArrayList<String> getData() {
    ArrayList<String> historyResult = new ArrayList<>();

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor =
        db.query(TABLE_NAME, new String[] {COL_HISTORY_TEXT}, null, null, null, null, null);
    if (cursor != null) {
      while (cursor.moveToNext()) {
        int indexResult = cursor.getColumnIndex(COL_HISTORY_TEXT);
        String singleResult = cursor.getString(indexResult);
        historyResult.add(singleResult);
      }
      cursor.close();
    } else {
      cursor.close();
    }
    db.close();

    return historyResult;
  }

  public void deleteTable() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("DELETE FROM " + TABLE_NAME);
  }
}
