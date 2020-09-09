package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.models.CaronaPega;
import com.ufc.com.carona_ufc.models.Usuario;
import com.ufc.com.carona_ufc.services.DirectionApi;

import java.util.ArrayList;
import java.util.List;

public class PegarCaronaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> arrayPoints;
    TextView tvnomeMotoristaCarona;
    TextView tvTelefone;
    TextView tvCaronasOferecidas;
    TextView tvDuracaoCarona;
    TextView tvDistanciaCarona;
    TextView tvDataCarona;
    TextView tvHoraCarona;
    TextView tvQtdVagasCarona;
    TextView tvCheckBoxHelpCarona;
    Button btnPegarCarona;
    ImageView btnWhatsApp;
    ImageView fotoMotorista;
    TextView tvAvaliacao;
    TextView tvValorCarona;
    Carona carona;
    private List<Polyline> polylines;
    CaronaAdapter caronaAdapter;
    ProgressDialog progressLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegar_carona);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCarona);
        mapFragment.getMapAsync(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));
        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();
        caronaAdapter = new CaronaAdapter(getBaseContext());

        //progressDialog
        progressLoad = new ProgressDialog(this);
        progressLoad.setTitle("Aguarde um momento...");
        progressLoad.setMessage("Carregando o mapa...");
        progressLoad.show();

        fotoMotorista = findViewById(R.id.fotoMotoristaCarona);
        tvValorCarona = findViewById(R.id.tvValorCarona);
        tvnomeMotoristaCarona = findViewById(R.id.tvNomeMotoristaCarona);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvCaronasOferecidas = findViewById(R.id.tvCaronasOferecidas);
        tvDuracaoCarona = findViewById(R.id.tvDuracaoCarona);
        tvDistanciaCarona = findViewById(R.id.tvDistanciaCarona);
        tvDataCarona = findViewById(R.id.tvDataCarona);
        tvHoraCarona = findViewById(R.id.tvHoraCarona);
        tvQtdVagasCarona = findViewById(R.id.tvQtdVagasCarona);
        tvCheckBoxHelpCarona = findViewById(R.id.tvCheckBoxHelpCarona);
        btnPegarCarona = findViewById(R.id.btnPegarCarona);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);
        tvAvaliacao = findViewById(R.id.tvAvaliacao2);

        carona = getIntent().getExtras().getParcelable("carona");
        tvnomeMotoristaCarona.setText(carona.getIdMotorista());
        tvDataCarona.setText(carona.getData());
        tvHoraCarona.setText(carona.getHora());
        tvQtdVagasCarona.setText(String.valueOf(carona.getQtdVagas()));
        tvValorCarona.setText(carona.getValorCarona());


        setarDadosUser(carona);


        //Adicionar no banco das caronas pegas e diminuir uma vaga
        btnPegarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CaronaPega caronaPega = new CaronaPega(carona.getIdMotorista(), carona.getId(), FirebaseAuth.getInstance().getUid());
                FirebaseFirestore.getInstance().collection("/caronasPegas")
                        .add(caronaPega)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("teste", "Carona pega");
                                Toast.makeText(PegarCaronaActivity.this, "Você garantiu sua vaga!", Toast.LENGTH_SHORT).show();
                                FirebaseFirestore.getInstance().collection("caronas")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                                    Carona c = doc.toObject(Carona.class);
                                                    if (c.getId().equals(caronaPega.getIdCarona())) {
                                                        int qtd = c.getQtdVagas() - 1;
                                                        attQtdVagas(doc, qtd);
                                                    }
                                                }
                                            }
                                        });
                                FirebaseFirestore.getInstance().terminate().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                                //Voltar para a tela inicial
                                startActivity(new Intent(getBaseContext(), CaronaQuePegareiActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("teste", "Falha ao pegar a carona: " + e.getMessage());
                    }
                });
            }
        });

        //abrir whatsapp com numero e uma mensagem pré pronta
        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PegarCaronaActivity.this, "WhatsApp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attQtdVagas(DocumentSnapshot doc, int qtd) {
        FirebaseFirestore.getInstance().collection("caronas")
                .document(doc.getId())
                .update("qtdVagas", qtd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("teste", "Ok!");
                    }
                });
        FirebaseFirestore.getInstance().terminate().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void setarDadosUser(final Carona carona) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Usuario user = doc.toObject(Usuario.class);
                            if (user.getIdUser().equals(carona.getIdMotorista())) {
                                Picasso.get().load(user.getUrlFotoUser()).into(fotoMotorista);
                                tvnomeMotoristaCarona.setText(user.getNomeUser());
                                if (user.getTelefoneUser() == null || user.getTelefoneUser().equals("")) {
                                    user.setTelefoneUser("Não Informado");
                                }
                                tvTelefone.setText(user.getTelefoneUser());
                                tvAvaliacao.setText(String.valueOf(user.getAvaliacao()));
                            }
                        }
                    }
                });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);


        // Add a marker in myPosition
        LatLng startPosition = new LatLng(carona.getLatOrigem(), carona.getLngOrigem());
        MarkerOptions marker_start = new MarkerOptions();
        marker_start.position(startPosition).title(carona.getEnderecoSaida());
        googleMap.addMarker(marker_start);

        // Add a maker in PositionDestino
        LatLng stopPosition = new LatLng(carona.getLatDestino(), carona.getLngDestino());
        MarkerOptions marker_stop = new MarkerOptions();
        marker_stop.title(carona.getEnderecoChegada()).position(stopPosition)
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
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, tvDistanciaCarona, tvDuracaoCarona, carona);
        directionApi.drawRoute(startPosition, stopPosition);

        //Regular o zoom nos dois ponto
        final ArrayList<MarkerOptions> markers = new ArrayList<>();
        markers.add(marker_start);
        markers.add(marker_stop);


        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerOptions marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                //limitar que o usuario mova o mapa para fora da visao
                googleMap.setLatLngBoundsForCameraTarget(bounds);
                int padding = 75;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.animateCamera(cameraUpdate);
                progressLoad.dismiss();
            }
        });


    }


}