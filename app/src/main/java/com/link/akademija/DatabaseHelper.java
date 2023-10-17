package com.link.akademija;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Akademija.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POLAZNIK = "Polaznik";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_IME = "ime";
    public static final String COLUMN_PREZIME = "prezime";
    public static final String COLUMN_GODINA_UPISA = "godina_upisa";
    public static final String COLUMN_BROJ_POENA = "broj_poena";

    private static final String CREATE_TABLE_POLAZNIK =
            "CREATE TABLE " + TABLE_POLAZNIK + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_IME + " TEXT, "
                    + COLUMN_PREZIME + " TEXT, "
                    + COLUMN_GODINA_UPISA + " INTEGER, "
                    + COLUMN_BROJ_POENA + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POLAZNIK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLAZNIK);
        onCreate(db);
    }
}
