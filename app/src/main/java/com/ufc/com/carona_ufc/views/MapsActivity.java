package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.services.DirectionApi;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> arrayPoints;
    TextView etDurationKm;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etDurationKm = findViewById(R.id.tvDurationKm);
        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("latlng");
        double latSaida, lngSaida, latChegada, lngChegada;

        //LatLng Saida
        latSaida = bundle.getDouble("latSaida");
        lngSaida = bundle.getDouble("lngSaida");
        //LatLng Destino
        latChegada = bundle.getDouble("latChegada");
        lngChegada = bundle.getDouble("lngChegada");

        // Add a marker in myPosition
        LatLng startPosition = new LatLng(latSaida, lngSaida);
        MarkerOptions marker_start = new MarkerOptions();
        marker_start.position(startPosition).title("Minha posição");
        googleMap.addMarker(marker_start);

        // Add a maker in PositionDestino
        LatLng stopPosition = new LatLng(latChegada, lngChegada);
        MarkerOptions marker_stop = new MarkerOptions();
        marker_stop.title("Posição Destino").position(stopPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        googleMap.addMarker(marker_stop);

        //Adicionar um circulo em minha posição
        googleMap.addCircle(new CircleOptions().center(startPosition).radius(50).strokeWidth(3f)
                .strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));

        //Verificar a permissão
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        //Tracando a rota
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, etDurationKm);
        directionApi.drawRoute(startPosition, stopPosition);

        //Regular o zoom nos dois ponto
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        markers.add(marker_start);
        markers.add(marker_stop);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        //limitar que o usuario mova o mapa para fora da visao
        googleMap.setLatLngBoundsForCameraTarget(bounds);
        int padding = 80;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);

    }


}
