package com.ufc.com.carona_ufc.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.models.Carona;

import java.util.ArrayList;

public class PesquisarCaronaActivity extends AppCompatActivity {
    EditText edTextPesquisa;
    RecyclerView rvCaronasPesquisas;
    CaronaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_carona);

        final ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));

        rvCaronasPesquisas = findViewById(R.id.rvCaronasPesquisa);
        edTextPesquisa = findViewById(R.id.edTextPesquisa);
        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        adapter = new CaronaAdapter(getBaseContext());
        manager.setReverseLayout(false);
        manager.setOrientation(RecyclerView.VERTICAL);

        rvCaronasPesquisas.setAdapter(adapter);
        rvCaronasPesquisas.setLayoutManager(manager);

        buscarCaronasComAData();

        edTextPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                adapter.getListCaronas().clear();
            }
        });


    }

    private void buscarCaronasComAData() {
        FirebaseFirestore.getInstance().collection("caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Carona c = documentSnapshot.toObject(Carona.class);
                            if (c.getQtdVagas() > 0) {
                                adapter.add(c);
                            }
                        }
                    }
                });
    }

    private void filter(final String text) {
        final ArrayList<Carona> filteredList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Carona c = doc.toObject(Carona.class);
                            adapter.add(c);
                        }

                        for (Carona c : adapter.getListCaronas()) {
                            if (c.getEnderecoChegada().toLowerCase().contains(text.toLowerCase())) {
                                if (c.getQtdVagas() > 0) {
                                    filteredList.add(c);
                                }
                            }
                        }
                        adapter.filterList(filteredList);
                        adapter.notifyDataSetChanged();
                    }
                });

    }


}
