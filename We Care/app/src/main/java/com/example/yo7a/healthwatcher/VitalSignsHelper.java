package com.example.yo7a.healthwatcher;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VitalSignsHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VitalSignsDB.db";

    private static final String TABLE = "vital_signs";
    private static final String HEART_RATE = "heart_rate";
    private static final String OXYGEN_SATURATION = "oxygen_saturation";
    private static final String BLOOD_PRESSURE = "blood_pressure";
    private static final String RESPIRATION_RATE = "respiration_rate";
    private static final String NAME = "name";

    public VitalSignsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table vital_signs ( name text not null , heart_rate text , oxygen_saturation text ," +
                "blood_pressure text , respiration_rate text );";
        db.execSQL(createTable);
    }

    public boolean addData(String nm, String HR, String OS, String BP, String RR){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("addDataName", nm);

        if (!isValueExist(nm)){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, nm);
            contentValues.put(BLOOD_PRESSURE, BP);
            contentValues.put(HEART_RATE, HR);
            contentValues.put(OXYGEN_SATURATION, OS);
            contentValues.put(RESPIRATION_RATE, RR);

            long result = db.insert(TABLE, null, contentValues);

            db.close();

            if (result==-1){
                return false;
            }
            else {
                return true;
            }
        }
        else{
            //deleteTitle(nm);

            ContentValues contentValues = new ContentValues();
            long result=-1;

            if (BP!=""){
                contentValues.put(BLOOD_PRESSURE, BP);
                result += db.update(TABLE, contentValues, NAME + "=?", new String[]{nm});
            }
            if (HR!=""){
                contentValues.put(HEART_RATE, HR);
                result += db.update(TABLE, contentValues, NAME + "=?", new String[]{nm});
            }
            if (OS!=""){
                contentValues.put(OXYGEN_SATURATION, OS);
                result += db.update(TABLE, contentValues, NAME + "=?", new String[]{nm});
            }
            if (RR!=""){
                contentValues.put(RESPIRATION_RATE, RR);
                result += db.update(TABLE, contentValues, NAME + "=?", new String[]{nm});
            }

            db.close();

            if (result==-1){
                return false;
            }
            else {
                return true;
            }
        }
    }

    public boolean deleteTitle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, NAME + "=?", new String[]{name}) > 0;
    }

    public Cursor getListContents(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE + " WHERE " + NAME + " = '" +
                user + "';", null);
        return data;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS vital_signs";
        db.execSQL(query);
    }

    private boolean isValueExist(String value){
        String query = "SELECT * FROM " + TABLE + " WHERE " + NAME + " = ?";
        String[] whereArgs = {value};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        return count >= 1;
    }
}
