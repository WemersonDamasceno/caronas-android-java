package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ufc.com.carona_ufc.models.CaronasPegas;
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
    LinearLayout esconderbotoes;

    Carona carona;
    private List<Polyline> polylines;
    Button btnEditarCarona;
    Button btnExcluirCarona;
    CaronaAdapter caronaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegar_carona);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCarona);
        mapFragment.getMapAsync(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));
        arrayPoints = new ArrayList<>();
        polylines = new ArrayList<>();
        caronaAdapter = new CaronaAdapter();


        esconderbotoes = findViewById(R.id.esconderBotoes);
        fotoMotorista = findViewById(R.id.fotoMotoristaCarona);
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
        btnEditarCarona = findViewById(R.id.btnEditarCarona);
        btnExcluirCarona = findViewById(R.id.btnExcluirCarona);

        carona = getIntent().getExtras().getParcelable("carona");
        tvnomeMotoristaCarona.setText(carona.getIdMotorista());
        tvDataCarona.setText(carona.getData());
        tvHoraCarona.setText(carona.getHora());
        tvQtdVagasCarona.setText(String.valueOf(carona.getQtdVagas()));

        setarDadosUser(carona);

        if (carona.getIdMotorista().equals(FirebaseAuth.getInstance().getUid())) {
            btnPegarCarona.setVisibility(View.INVISIBLE);
            mostrarBotoes();
        }




        btnPegarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaronasPegas caronasPegas = new CaronasPegas(carona.getIdMotorista(), carona.getId(), FirebaseAuth.getInstance().getUid());
                FirebaseFirestore.getInstance().collection("/caronasPegas")
                        .add(caronasPegas)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i("teste", "carona pega");
                                carona.setQtdVagas(carona.getQtdVagas() - 1);
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

    private void mostrarBotoes() {
        Toast.makeText(this, "Você está oferecendo essa carona", Toast.LENGTH_SHORT).show();
        esconderbotoes.setVisibility(View.VISIBLE);


        btnExcluirCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //excluir carona e voltar para a tela de buscar caronas
                FirebaseFirestore.getInstance().collection("/caronas")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    Carona car = doc.toObject(Carona.class);
                                    if (car.getId().equals(carona.getId())) {
                                        deletarCarona(doc);
                                    }
                                }
                            }
                        });
            }
        });

        //Fazer isso amanha blz bonitão ;)
        btnEditarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pegar a carona e voltar para a tela de oferecer carona
                FirebaseFirestore.getInstance().collection("/caronas")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    Carona car = doc.toObject(Carona.class);
                                    if (car.getId().equals(carona.getId())) {
                                        editarCarona(doc);
                                    }
                                }
                            }
                        });
            }
        });

    }

    private void editarCarona(DocumentSnapshot doc) {
        Intent intent = new Intent(getBaseContext(), OferecerCaronaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("caronaEditar", carona);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    private void deletarCarona(DocumentSnapshot doc) {
        FirebaseFirestore.getInstance().collection("/caronas")
                .document(doc.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("deletar", "deletado");
                        Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("erro", "Erro ao excluir carona: " + e.getMessage());
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
                                tvTelefone.setText(user.getTelefoneUser());
                                //falta a avaliação e a quantidade de caronas que ele deu.
                            }
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
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

        //Tracando a rota
        DirectionApi directionApi = new DirectionApi(polylines, googleMap, tvDistanciaCarona, tvDuracaoCarona, carona);
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
