package com.example.ombr_ronan.tourist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Marqueur extends AppCompatActivity {

    BDD bdd;

    int cpt = 0;

    String name, latitude, longitude;

    EditText etName;

    ArrayList<Position> listMarqueur;
    ArrayList<Position> listAfficher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marqueur);

        //Activation de la base de données
        bdd = new BDD(this);
        bdd.open();

        //Initialisation de la futur liste d'affichage
        listAfficher = new ArrayList<Position>();

        initAffichage();
    }

    //Méthode permettant d'initialiser/rafraichir l'affichage
    private void initAffichage() {

        //Récupération de tous les locations disponibles
        listMarqueur = bdd.getAllLocations();

        //Récupération de la zone de rajout des locations
        LinearLayout origin = findViewById(R.id.listMarqueur);


        //Définition des paramètres des futur boutons
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = 50;
        params.height = 50;


        //Cas de rafraichissage de la page
        if(origin.getChildCount() > 0)
            origin.removeAllViews();

        //Parcours des locations (si existant)
        if(listMarqueur != null) {
            for(Position pos : listMarqueur) {

                //Création d'une nouvelle ligne pour le marqueur
                LinearLayout marqueur = new LinearLayout(this);
                marqueur.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                marqueur.setOrientation(LinearLayout.HORIZONTAL);

                //Récupération des informations de la position
                name = pos.getNom();
                latitude = pos.getLatitude();
                longitude = pos.getLongitude();

                marqueur.setId(cpt);

                //Création des Views du marqueur
                etName= new EditText(this);
                etName.setText(name);
                final TextView etLatitude = new TextView(this);
                etLatitude.setText(new DecimalFormat("#.##").format(Double.parseDouble(latitude)));
                final TextView etLongitude = new TextView(this);
                etLongitude.setText(new DecimalFormat("#.##").format(Double.parseDouble(longitude)));


                //Bouton Supprimer
                Button delete = new Button(this);
                delete.setBackgroundResource(R.drawable.trash);
                delete.setOnClickListener(new View.OnClickListener() {

                    String n = name;
                    String lat = latitude;
                    String lon = longitude;

                    @Override
                    public void onClick(View v) {
                        bdd.deleteLocation(n, lat, lon);
                        initAffichage();
                    }
                });

                delete.setLayoutParams(params);
                delete.setText("");


                //Bouton Afficher (dans la map)
                final Button affiche = new Button(this);
                affiche.setBackgroundResource(R.drawable.ok);
                affiche.setOnClickListener(new View.OnClickListener() {

                    int act = cpt;

                    @Override
                    public void onClick(View v) {

                        //On ajoute / enlève à la liste des marqueurs que l'on affichera sur la map
                        if(affiche.getText().toString().equals(".")) {
                            listAfficher.add(listMarqueur.get(act));
                            affiche.setBackgroundResource(R.drawable.no);
                            affiche.setText("");
                        }
                        else {
                            //On parcours la liste d'affichage pour retrouver celui que l'on souhaite supprimer
                            for(int i = 0; i < listAfficher.size(); i++) {
                                if(listAfficher.get(i).getNom() == listMarqueur.get(act).getNom() &&
                                        listAfficher.get(i).getLatitude() == listMarqueur.get(act).getLatitude() &&
                                        listAfficher.get(i).getLongitude() == listMarqueur.get(act).getLongitude()) {

                                    listAfficher.remove(listAfficher.get(i));
                                    affiche.setBackgroundResource(R.drawable.ok);
                                    affiche.setText(".");
                                }
                            }
                        }
                    }
                });

                affiche.setLayoutParams(params);
                affiche.setText(".");


                //Création d'espace entre les éléments
                TextView spaceA, spaceB, spaceC, spaceD, spaceE;
                spaceA = new TextView(this);
                spaceB = new TextView(this);
                spaceC = new TextView(this);
                spaceD = new TextView(this);
                spaceE = new TextView(this);

                spaceA.setText("    ");
                spaceB.setText("     ");
                spaceC.setText("           ");
                spaceD.setText("    ");
                spaceE.setText("    ");

                //Ajout à la ligne
                marqueur.addView(etName);
                marqueur.addView(spaceA);
                marqueur.addView(etLatitude);
                marqueur.addView(spaceB);
                marqueur.addView(etLongitude);
                marqueur.addView(spaceC);

                marqueur.addView(delete);
                marqueur.addView(spaceD);
                marqueur.addView(spaceE);
                marqueur.addView(affiche);

                origin.addView(marqueur);

                //Ligne suivante
                cpt++;
            }
        }
        //Cas d'abence de location
        else {
            TextView tv = new TextView(this);
            tv.setText("Aucun marqueur disponible");
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            origin.addView(tv);
        }
    }

    /*******************ACTION des boutons*******************/

    //Action du bouton Retour
    public void retour(View view) {
        this.finish();
    }

    //Action du bouton Carte
    public void goMap(View view) {
        bdd.close();
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra("list", listAfficher);

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

}
