package com.ufc.com.carona_ufc.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.models.CaronaPega;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CaronaQuePegareiActivity extends AppCompatActivity {
    private RecyclerView rvCaronasPegarei;
    private CaronaAdapter caronaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona_que_pegarei);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));
        bar.setTitle("Minhas Caronas");


        rvCaronasPegarei = findViewById(R.id.rvCaronasQuePegarei);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);
        caronaAdapter = new CaronaAdapter(getBaseContext());
        rvCaronasPegarei.setAdapter(caronaAdapter);
        rvCaronasPegarei.setLayoutManager(layoutManager);

        buscarCaronasQuePegarei(FirebaseAuth.getInstance().getUid());


    }


    private void buscarCaronasQuePegarei(final String idUser) {
        FirebaseFirestore.getInstance().collection("caronasPegas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            final CaronaPega caronaPega = doc.toObject(CaronaPega.class);
                            if (caronaPega.getIdUser().equals(idUser)) {
                                FirebaseFirestore.getInstance().collection("caronas")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                                    Carona carona = doc.toObject(Carona.class);
                                                    if (carona.getId().equals(caronaPega.getIdCarona())) {
                                                        compararDatasEAddLista(carona);
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    //Comparar se a data da carona é a mesma data de hoje
    private void compararDatasEAddLista(Carona carona) {
        Calendar dataHoje = Calendar.getInstance();
        Calendar dataCarona = new GregorianCalendar();
        String data = carona.getData();
        String dia = data.substring(0, 2);
        String mes = data.substring(3, 5);
        String ano = data.substring(6, 10);
        dataCarona.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
        dataCarona.set(Calendar.MONTH, Integer.parseInt(mes));
        dataCarona.set(Calendar.YEAR, Integer.parseInt(ano));
        //Pegar as caronas somente de hoje ou de dias seguintes
        if (dataCarona.after(dataHoje) || dataCarona.equals(dataHoje)) {
            caronaAdapter.add(carona);
            caronaAdapter.notifyDataSetChanged();
        }
    }
}
