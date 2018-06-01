package com.example.ombr_ronan.tourist;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener{

    LocationManager manager;
    double latitude, longitude;

    BDD bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Activation de la base de données
        bdd = new BDD(this);
        bdd.open();

        //Récupération du GPS et écoute de ses modifications
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, MainActivity.this);
        } catch (SecurityException e) { }
    }

    /*******************ACTION des boutons*******************/

    //Action du Bouton ajoutant la position de l'utilisateur
    public void addPosition(View v) {

        //GPS actif
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder addLocalisation = new AlertDialog.Builder(this);

            //Futur emplacement du nom de la Position actuelle
            final EditText nomLocalisation = new EditText(this);
            nomLocalisation.setInputType(InputType.TYPE_CLASS_TEXT);

            //Création de la pop up d'ajout de position
            addLocalisation
                    .setView(nomLocalisation)
                    .setMessage("Nommer votre position")
                    .setCancelable(true)
                    //Action si appui sur le bouton Valider
                    .setPositiveButton("Valider ",
                            new DialogInterface.OnClickListener() {

                                //Ajout de la position à la base de données
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    //Vérifie qu'un nom est saisie pour la Position
                                    if(nomLocalisation.getText().toString().replaceAll(" ", "").equals("")) {
                                        Toast.makeText(MainActivity.this, "Erreur : Absence de nom", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Position local = new Position(nomLocalisation.getText().toString(), ""+latitude, ""+longitude);
                                        bdd.addLocation(local);
                                        Toast.makeText(MainActivity.this, "Position ajoutée", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    )
                    //Action si appui sur le bouton Annuler
                    .setNegativeButton("Annuler ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {}
                            }
                    )
                    //Action si appui sur la touche retour
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {}
                    });

            addLocalisation.create().show();
        }
        else {
            Toast.makeText(this, "GPS inactif.", Toast.LENGTH_LONG).show();
        }


    }

    //Ouverture de la page Marqueur
    public void openMarqueur(View v) {
        bdd.close();
        Intent intent = new Intent(this, Marqueur.class);
        startActivity(intent);
    }

    /*******************SURCHARGE vie de l'application*******************/

    @Override
    public void onDestroy() {
        bdd.close();
        super.onDestroy();

    }

    @Override
    public void onPause() {
        bdd.close();
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        bdd.close();
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        bdd.open();
    }

    /*******************SURCHARGE LocationListener*******************/
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
