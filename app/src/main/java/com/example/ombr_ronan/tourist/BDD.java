package com.example.ombr_ronan.tourist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class BDD {

    private static final int VERSION_BDD = 1;

    private SQLiteDatabase bdd_location;
    Database_location DB_location;

    //Définition du nom de la base de données
    private static final String TABLE_CLASSE = "table_coordonnée";

    //Définition des noms de colonnes de la base de données
    private static final String COL_NOM = "nom";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";

    //Définition des numéros de colonne de la base de données
    private static final int NUM_COL_NOM = 0;
    private static final int NUM_COL_LATITUDE = 1;
    private static final int NUM_COL_LONGITUDE = 2;


    public BDD(Context context){
        DB_location = new Database_location(context, TABLE_CLASSE, null, VERSION_BDD);
    }

    //Ouvertire de la base de données
    public void open() {
        bdd_location = DB_location.getWritableDatabase();
        bdd_location.execSQL(DB_location.getCreate());
    }

    //Suppression de la base de données
    public void destroy() {
        DB_location.onUpgrade(bdd_location, VERSION_BDD, VERSION_BDD);
    }

    //Fermeture de la base de données
    public void close() {
        bdd_location.close();
    }

    //Récupération de la base de données
    public SQLiteDatabase getBDD() { return bdd_location; }

    //Ajout d'une Location par nom, latitude et longitude
    public long addLocation(String nom, String latitude, String longitude) {
        ContentValues values = new ContentValues();
        values.put(COL_NOM, nom);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);
        return bdd_location.insert(TABLE_CLASSE, null, values);
    }

    //Ajout d'une Location par Position
    public long addLocation(Position pos) {
        ContentValues values = new ContentValues();
        values.put(COL_NOM, pos.getNom());
        values.put(COL_LATITUDE, pos.getLatitude());
        values.put(COL_LONGITUDE, pos.getLongitude());
        return bdd_location.insert(TABLE_CLASSE, null, values);
    }

    //Converti un curseur d'une Location en Position
    @Nullable
    private Position cursorToLocation(Cursor c){
        if (c.getCount() == 0) return null;

        c.moveToFirst();

        Position position = new Position(
                c.getString(NUM_COL_NOM),
                c.getString(NUM_COL_LATITUDE),
                c.getString(NUM_COL_LONGITUDE)
        );

        c.close();

        return position;
    }

    //Converti un curseur de location en liste de Position
    @Nullable
    private ArrayList<Position> cursorToLocations(Cursor c) {
        if (c.getCount() == 0) return null;

        ArrayList<Position> locations = new ArrayList<Position>();

        c.moveToFirst();

        //Parcours du curseur
        while(!c.isAfterLast()) {
            Position location = new Position(c.getString(NUM_COL_NOM), c.getString(NUM_COL_LATITUDE), c.getString(NUM_COL_LONGITUDE));

            locations.add(location);
            c.moveToNext();
        }

        c.close();

        return locations;
    }

    //Récupére toute les locations disponible
    @Nullable
    public ArrayList<Position> getAllLocations() {
        Cursor c = bdd_location.query(TABLE_CLASSE, new String[] {"*"}, null, null, null, null, null);
        return cursorToLocations(c);
    }

    //Met à jour une location selon ses anciennes informations
    public int updateLocation(Position oldLocation, Position newLocation) {

        ContentValues values = new ContentValues();
        values.put(COL_NOM, newLocation.getNom());
        values.put(COL_LATITUDE, newLocation.getLatitude());
        values.put(COL_LONGITUDE, newLocation.getLongitude());

        return bdd_location.update(TABLE_CLASSE, values, COL_NOM + " LIKE \"" + oldLocation.getNom() + "\" and " + COL_LATITUDE + " LIKE \"" + oldLocation.getLatitude() +"\" and " + COL_LONGITUDE + " LIKE \"" + oldLocation.getLongitude() +"\"", null);
    }

    //Supprime une location
    public boolean deleteLocation(String nom, String latitude, String longitude) {
        return bdd_location.delete(TABLE_CLASSE, COL_NOM + " LIKE \"" + nom + "\" and " + COL_LATITUDE + " LIKE \"" + latitude +"\" and " + COL_LONGITUDE + " LIKE \"" + longitude +"\"", null) > 0;
    }

}
