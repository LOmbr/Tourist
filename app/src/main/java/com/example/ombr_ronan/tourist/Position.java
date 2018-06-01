package com.example.ombr_ronan.tourist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Position implements Serializable {

    private String nom;
    private String latitude, longitude;

    //Constructeur par defaut : Paris
    public Position() {
        this("Paris-default", "48.866667", "2.333333");//Coordonnée de Paris
    }

    //Constructeur par nom, latitude et longitude
    public Position(String nom, String latitude, String longitude) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Constructeur par recopie
    public Position(Position clone) {
        this.nom = clone.nom;
        this.latitude = clone.latitude;
        this.longitude = clone.longitude;
    }


    public String getNom() {
        return nom;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
