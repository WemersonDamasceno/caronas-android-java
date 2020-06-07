package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.services.DirectionApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ConfirmarCaronaActivity extends AppCompatActivity implements OnMapReadyCallback {
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

    Carona carona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_carona);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));

        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();

        tvDistanciaConfirm = findViewById(R.id.tvDistanciaConfirm);
        tvDuracaoConfirm = findViewById(R.id.tvDuracaoConfirm);
        tvDataConfirm = findViewById(R.id.tvDataConfirm);
        tvHoraConfirm = findViewById(R.id.tvHoraConfirm);
        tvQtdVagasConfirm = findViewById(R.id.tvQtdVagasConfirm);
        tvCheckBoxHelp = findViewById(R.id.tvCheckBoxHelpConfirm);
        btnCaronaConfirm = findViewById(R.id.btnCaronaConfirm);

        //pegando os dados da outra activity
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        carona = bundle.getParcelable("carona");



        //Enviar dados para confirmar
        btnCaronaConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se o ID for nulo é pq a carona esta sendo criada
                if (carona.getId() == null) {
                    Log.i("teste", "Essa carona será salva: " + carona.getId());
                    carona.setId(UUID.randomUUID().toString());
                    //salvar no banco e seguir para a tela principal
                    salvarCarona(carona);
                    Toast.makeText(ConfirmarCaronaActivity.this, "Carona criada com sucesso", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), ProcurarCaronaActivity.class);
                    startActivity(intent);
                    finish();
                }
                //Se não for é pq esta sendo editada
                else {
                    Log.i("teste", "Essa carona será editada: " + carona.getId());
                    editarCarona(carona);
                    //finish();
                }
            }
        });

    }

    private void editarCarona(final Carona carona1) {
        //Editar a carona
        FirebaseFirestore.getInstance().collection("/caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Carona c = doc.toObject(Carona.class);
                            if (c.getId().equals(carona1.getId())) {
                                updateInFirebase(doc);
                            }
                        }
                    }
                });
    }

    private void updateInFirebase(DocumentSnapshot doc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("/caronas")
                .document(doc.getId()).set(carona).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("teste", "Updated completed");
                startActivity(new Intent(getBaseContext(), ProcurarCaronaActivity.class));
                //finish();
            }
        });
        db.terminate().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getBaseContext(), ProcurarCaronaActivity.class));
            }
        });

       /* FirebaseFirestore.getInstance().collection("/caronas")
                .document(doc.getId())
                .update("checkBoxHelp", carona.isCheckBoxHelp(),
                        "data", carona.getData(),
                        "hora", carona.getHora(),
                        "enderecoChegada", carona.getEnderecoChegada(),
                        "enderecoSaida", carona.getEnderecoSaida(),
                        "qtdVagas",  carona.getQtdVagas(),
                        "latDestino", carona.getLatDestino(),
                        "lngDestino", carona.getLngDestino(),
                        "latOrigem", carona.getLatOrigem(),
                        "lngOrigem", carona.getLngOrigem(),
                        "horaChegadaprox", carona.getHoraChegadaprox()
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("teste", "Updated completed");
                startActivity(new Intent(getBaseContext(), ProcurarCaronaActivity.class));
                finish();
            }
        });*/
    }



    public void salvarCarona(Carona carona) {
        FirebaseFirestore.getInstance().collection("caronas")
                .add(carona)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("teste", "Sucesso ao add a carona no banco");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "Falha ao add a carona no banco: " + e.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);

        double latSaida, lngSaida, latChegada, lngChegada;

        //LatLng Saida
        latSaida = carona.getLatOrigem();
        lngSaida = carona.getLngOrigem();
        LatLng latLngSaida = new LatLng(latSaida, lngSaida);

        //LatLng Destino
        latChegada = carona.getLatDestino();
        lngChegada = carona.getLngDestino();
        LatLng latLngChegada = new LatLng(latChegada, lngChegada);

        //String origem e destino
        String origem = carona.getEnderecoSaida();
        String destino = carona.getEnderecoChegada();

        //Pegar os dados para confirmar
        tvHoraConfirm.setText(carona.getHora());
        tvDataConfirm.setText(carona.getData());
        tvQtdVagasConfirm.setText(String.valueOf(carona.getQtdVagas()));
        boolean checkBox = carona.isCheckBoxHelp();
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
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, tvDistanciaConfirm, tvDuracaoConfirm, carona);
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