package com.example.ombr_ronan.tourist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_location extends SQLiteOpenHelper {

    private static final String TABLE_CLASSE = "table_coordonnée";
    private static final String COL_NOM = "nom";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";

    private static final String CREATE_BDD = "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSE + " ("
            + COL_NOM + " TEXT NOT NULL, "
            + COL_LATITUDE + " TEXT NOT NULL, "
            + COL_LONGITUDE + " TEXT NOT NULL)";

    public Database_location(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    public String getCreate() {
        return CREATE_BDD;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CLASSE + ";");
        onCreate(db);
    }
}
