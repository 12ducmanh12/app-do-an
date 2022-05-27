package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String HISTORY_TABLE = "HISTORY_TABLE";
    public static final String TEMP_TABLE = "TEMP_TABLE";
    public static final String HUMID_TABLE = "HUMID_TABLE";
    public static final String LIGHT_TABLE = "LIGHT_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME_CREATE = "TIME_CREATE";
    public static final String COLUMN_DATE_CREATE = "DATE_CREATE";
    public static final String COLUMN_TEMPER = "TEMPER";
    public static final String COLUMN_HUMID = "HUMID";
    public static final String COLUMN_LIGHT = "LIGHT";

    public static final String COLUMN_DATETIME = "DATETIME";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "SmartFarm.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + HISTORY_TABLE + " " +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIME_CREATE + " STRING, "
                + COLUMN_DATE_CREATE + " STRING, "
                + COLUMN_TEMPER + " DOUBLE, "
                + COLUMN_HUMID + " INT, "
                + COLUMN_LIGHT + " DOUBLE, "
                + COLUMN_DATETIME + " TEXT )";

        db.execSQL(sql);

        // temperature

        sql = "CREATE TABLE " + TEMP_TABLE + " " +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEMPER + " DOUBLE, "
                + COLUMN_DATETIME + " TEXT )";

        db.execSQL(sql);

        //humid

        sql = "CREATE TABLE " + HUMID_TABLE + " " +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_HUMID + " INT, "
                + COLUMN_DATETIME + " TEXT )";

        db.execSQL(sql);

        // light

        sql = sql = "CREATE TABLE " + LIGHT_TABLE + " " +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LIGHT + " INT, "
                + COLUMN_DATETIME + " TEXT )";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(History history){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO "+ HISTORY_TABLE +
                " ("+COLUMN_TIME_CREATE+"," +COLUMN_DATE_CREATE+","+COLUMN_TEMPER+","+COLUMN_HUMID+","+COLUMN_DATETIME+")"
                + " VALUES (\"" +history.getTime()+"\",\""+history.getDate()+"\","+history.getTemp()+","+history.getHumid()+", datetime(\"now\",\"localtime\"))";

        db.execSQL(sql);
        return true;
    }

    public boolean execQuery(String sql){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        return true;
    }

    public boolean addOne(Light light){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO "+ TEMP_TABLE +
                " ("+COLUMN_TEMPER+","+COLUMN_DATETIME+")"
                + " VALUES (" +light.getLight()+", datetime(\"now\",\"localtime\"))";

        db.execSQL(sql);
        return true;
    }

    public boolean addOne(Temp temp){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO "+ TEMP_TABLE +
                " ("+COLUMN_TEMPER+","+COLUMN_DATETIME+")"
                + " VALUES (" +temp.getTemp()+", datetime(\"now\",\"localtime\"))";

        db.execSQL(sql);
        return true;
    }

    public boolean addOne(Humid humid){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO "+ HUMID_TABLE +
                " ("+COLUMN_HUMID+","+COLUMN_DATETIME+")"
                + " VALUES (" +humid.getHumid()+", datetime(\"now\",\"localtime\"))";

        db.execSQL(sql);
        return true;
    }


    public ArrayList<History> getHistoryLast3days(){

        ArrayList<History> historyList = new ArrayList<>();

        String sql = "SELECT * FROM "+ HISTORY_TABLE + " WHERE "+ COLUMN_DATETIME +" BETWEEN datetime(\"now\",\"localtime\",\"-3 days\") AND datetime(\"now\",\"localtime\") ORDER BY ID DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst())
        {
            do {
                int historyID = cursor.getInt(0);
                String time = cursor.getString(1);
                String date = cursor.getString(2);
                Double temp = cursor.getDouble(3);
                Integer humid = cursor.getInt(4);


                History history = new History(historyID, time, date, temp, humid);

                historyList.add(history);
            }while (cursor.moveToNext());
        }
        else
        {
            // failure. Do not add anything to the list
        }

        cursor.close();
        db.close();
        return historyList;
    }
}
