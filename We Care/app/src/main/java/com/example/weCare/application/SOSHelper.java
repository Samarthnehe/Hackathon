package com.example.weCare.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SOSHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SOSDB.db";

    private static final String SOS_TABLE = "sos";
    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_PHONE = "contact_phone";

    public SOSHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean addData(String name, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        if (!isValueExist(name)){
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_NAME, name);
            contentValues.put(CONTACT_PHONE, phone);

            long result = db.insert(SOS_TABLE, null, contentValues);

            db.close();

            if (result==-1){
                return false;
            }
            else {
                return true;
            }
        }
        else{
            String query = "UPDATE " + SOS_TABLE + " SET " + CONTACT_PHONE + " = '" +
                    phone + "' WHERE " + CONTACT_NAME + " = '" + name + "';";
            db.execSQL(query);
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + SOS_TABLE, null);
        return data;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table sos ( contact_name text not null , contact_phone text not null);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS sos";
        db.execSQL(query);
    }

    private boolean isValueExist(String value){
        String query = "SELECT * FROM " + SOS_TABLE + " WHERE " + CONTACT_NAME + " = ?";
        String[] whereArgs = {value};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }
}
