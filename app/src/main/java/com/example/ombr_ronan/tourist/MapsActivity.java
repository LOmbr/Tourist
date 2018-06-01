package com.example.ombr_ronan.tourist;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<Position> listAff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Récupération, si elle existe, de la liste à afficher
        if(getIntent().getSerializableExtra("list") != null)
            listAff = (ArrayList<Position>) getIntent().getSerializableExtra("list");
        else
            listAff = new ArrayList<Position>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Affichage de tous les points dans la liste d'affichage
        for(Position pos : listAff) {
            //Défini la latitude et la longitude du point
            LatLng act = new LatLng(Double.parseDouble(pos.getLatitude()), Double.parseDouble(pos.getLongitude()));
            //Ajoute le point à la map en lui ajoutant un nom
            mMap.addMarker(new MarkerOptions().position(act).title(pos.getNom()));
        }
    }

    /*******************SURCHARGE vie de l'application*******************/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", listAff);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listAff = (ArrayList<Position>) savedInstanceState.getSerializable("list");
    }
}
