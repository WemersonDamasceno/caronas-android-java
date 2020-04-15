package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> arrayPoints;
    private List<Polyline> polylines;
    //MostrarDados
    TextView tvDistanciaConfirm;
    TextView tvDataConfirm;
    TextView tvHoraConfirm;
    TextView tvQtdVagasConfirm;
    TextView tvCheckBoxHelp;
    TextView tvDuracaoConfirm;
    Button btnCaronaConfirm;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));

        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();


        tvDistanciaConfirm = findViewById(R.id.tvDistanciaConfirm);
        tvDuracaoConfirm = findViewById(R.id.tvDuracaoConfirm);
        tvDataConfirm = findViewById(R.id.tvDataConfirm);
        tvHoraConfirm = findViewById(R.id.tvHoraConfirm);
        tvQtdVagasConfirm = findViewById(R.id.tvQtdVagasConfirm);
        tvCheckBoxHelp = findViewById(R.id.tvCheckBoxHelp);
        btnCaronaConfirm = findViewById(R.id.btnCaronaConfirm);

        //pegando osdados da outra activity
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("latlng");

        //Enviar dados para confirmar
        btnCaronaConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salvar no banco e seguir para a tela principal
                Intent intent = new Intent(v.getContext(), ProcurarCaronaActivity.class);
                intent.putExtra("dados", bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);

        double latSaida, lngSaida, latChegada, lngChegada;

        //LatLng Saida
        latSaida = bundle.getDouble("latSaida");
        lngSaida = bundle.getDouble("lngSaida");
        //LatLng Destino
        latChegada = bundle.getDouble("latChegada");
        lngChegada = bundle.getDouble("lngChegada");
        //String origem e destino
        String origem = bundle.getString("origem");
        String destino = bundle.getString("destino");

        //Pegar os dados para confirmar
        tvHoraConfirm.setText(bundle.getString("hora"));
        tvDataConfirm.setText(bundle.getString("data"));
        tvQtdVagasConfirm.setText(bundle.getString("qtdVagas"));
        boolean checkBox = bundle.getBoolean("check");
        if (checkBox) {
            tvCheckBoxHelp.setText("Precisa de ajuda ( X ) Sim Não ( )");
        } else {
            tvCheckBoxHelp.setText("Precisa de ajuda ( ) Sim Não ( X )");
        }

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
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, tvDistanciaConfirm, tvDuracaoConfirm);
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
        int padding = 90;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);


    }


}
