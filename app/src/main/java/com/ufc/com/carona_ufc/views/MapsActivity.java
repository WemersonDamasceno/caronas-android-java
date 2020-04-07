package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ufc.com.carona_ufc.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //para desenhar no mapa
    Polyline drawPolyline;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("latlng");
        double latSaida, lngSaida, latChegada, lngChegada;
        //saida
        latSaida = bundle.getDouble("latSaida");
        lngSaida = bundle.getDouble("lngSaida");
        //destino
        latChegada = bundle.getDouble("latChegada");
        lngChegada = bundle.getDouble("lngChegada");
        // Add a marker in myPosition
        LatLng myPosition = new LatLng(latSaida, lngSaida);
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Minha Posição"));
        // Add a maker in PositionDestino
        LatLng positionDestino = new LatLng(latChegada, lngChegada);
        mMap.addMarker(new MarkerOptions().position(positionDestino).title("Posicao Destino"));
        //Zomm do Mapa
        float zoomLevel = 15.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, zoomLevel));
        //tracar uma linha entre os dois pontos
        mMap.addPolyline(
                new PolylineOptions().add(myPosition).add(positionDestino)
                        .color(Color.RED)
                        .width(2f)
        );
        //Adicionar um circulo em minha posição
        mMap.addCircle(new CircleOptions().center(myPosition).radius(50).strokeWidth(3f)
                .strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));



    }
}
