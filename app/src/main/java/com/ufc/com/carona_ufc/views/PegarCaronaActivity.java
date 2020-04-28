package com.ufc.com.carona_ufc.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.services.DirectionApi;

import java.util.ArrayList;
import java.util.List;

public class PegarCaronaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> arrayPoints;
    TextView tvnomeMotoristaCarona;
    TextView tvTelefone;
    TextView tvCaronasOferecidas;
    LinearLayout llEstrelas;
    TextView tvDuracaoCarona;
    TextView tvDistanciaCarona;
    TextView tvDataCarona;
    TextView tvHoraCarona;
    TextView tvQtdVagasCarona;
    TextView tvCheckBoxHelpCarona;
    Button btnPegarCarona;
    Carona carona;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegar_carona);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCarona);
        mapFragment.getMapAsync(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));
        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();


        tvnomeMotoristaCarona = findViewById(R.id.tvNomeMotoristaCarona);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvCaronasOferecidas = findViewById(R.id.tvCaronasOferecidas);
        llEstrelas = findViewById(R.id.llEstrelas);
        tvDuracaoCarona = findViewById(R.id.tvDuracaoCarona);
        tvDistanciaCarona = findViewById(R.id.tvDistanciaCarona);
        tvDataCarona = findViewById(R.id.tvDataCarona);
        tvHoraCarona = findViewById(R.id.tvHoraCarona);
        tvQtdVagasCarona = findViewById(R.id.tvQtdVagasCarona);
        tvCheckBoxHelpCarona = findViewById(R.id.tvCheckBoxHelpCarona);
        btnPegarCarona = findViewById(R.id.btnPegarCarona);

        carona = getIntent().getExtras().getParcelable("carona");
        tvnomeMotoristaCarona.setText(carona.getIdMotorista());
        //tvTelefone.setText(pessoa.getTelefone);
        // tvCaronasOferecidas.setText(pessoa.getQtdCaronasOferecidas);
        tvDataCarona.setText(carona.getData());
        tvHoraCarona.setText(carona.getHora());
        tvQtdVagasCarona.setText(carona.getQtdVagas() + "");


        btnPegarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PegarCaronaActivity.this, "Olha ai mermão", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);

        if (carona.isCheckBoxHelp()) {
            tvCheckBoxHelpCarona.setText("Precisa de ajuda ( X ) Sim Não ( )");
        } else {
            tvCheckBoxHelpCarona.setText("Precisa de ajuda ( ) Sim Não ( X )");
        }


        // Add a marker in myPosition
        LatLng startPosition = new LatLng(carona.getLatLngOrigem().latitude, carona.getLatLngOrigem().longitude);
        MarkerOptions marker_start = new MarkerOptions();
        marker_start.position(startPosition).title("Minha posição");
        googleMap.addMarker(marker_start);

        // Add a maker in PositionDestino
        LatLng stopPosition = new LatLng(carona.getLatLngDestino().latitude, carona.getLatLngDestino().longitude);
        MarkerOptions marker_stop = new MarkerOptions();
        marker_stop.title("Posição Destino").position(stopPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        googleMap.addMarker(marker_stop);

        //Adicionar um circulo em minha posição
        googleMap.addCircle(new CircleOptions().center(startPosition).radius(50).strokeWidth(3f)
                .strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));

        //Tracando a rota
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, tvDistanciaCarona, tvDuracaoCarona);
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
