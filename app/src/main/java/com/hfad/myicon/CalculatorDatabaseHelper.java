package com.hfad.myicon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class CalculatorDatabaseHelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "calculatorResult";
  public static final int DB_VERSION = 2;
  public static final String TABLE_NAME = "RESULT";
  public static final String COL_ID = "_id";
  public static final String COL_HISTORY_TEXT = "HISTORY_TEXT";
  public static final String COL_DATE_TIME = "DATE_TIME";

  CalculatorDatabaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "HISTORY_TEXT TEXT, "
            + "DATE_TIME TEXT);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

  public void insertData(String numberResult, String dateTime) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_HISTORY_TEXT, numberResult);
    contentValues.put(COL_DATE_TIME, dateTime);
    db.insert(TABLE_NAME, null, contentValues);
    db.close();
  }

  public Map<Integer, ArrayList<String>> getTextData() {
    SQLiteDatabase db = this.getReadableDatabase();
    Map<Integer, ArrayList<String>> historyResult = new HashMap<>();

    Cursor cursor =
        db.query(
            TABLE_NAME,
            new String[] {COL_ID, COL_HISTORY_TEXT, COL_DATE_TIME},
            null,
            null,
            null,
            null,
            null);
    if (cursor != null) {
      while (cursor.moveToNext()) {
        int indexText = cursor.getColumnIndex(COL_HISTORY_TEXT);
        int indexId = cursor.getColumnIndex(COL_ID);
        int indexDateTime = cursor.getColumnIndex(COL_DATE_TIME);
        int id = cursor.getInt(indexId);
        String Text = cursor.getString(indexText);
        String DateTime = cursor.getString(indexDateTime);
        historyResult.put(id, new ArrayList<>(Arrays.asList(Text, DateTime)));
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
    db.close();
  }
}
