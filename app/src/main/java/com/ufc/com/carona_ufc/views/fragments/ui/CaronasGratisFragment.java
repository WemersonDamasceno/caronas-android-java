package com.ufc.com.carona_ufc.views.fragments.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.controller.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.model.Carona;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaronasGratisFragment extends Fragment {
    private CaronaAdapter caronaAdapter;
    private Button btnCompartilhar;
    private LinearLayout layoutLost;
    public CaronaAdapter getCaronaAdapter() {
        return caronaAdapter;
    }

    public void setCaronaAdapter(CaronaAdapter caronaAdapter) {
        this.caronaAdapter = caronaAdapter;
    }

    public CaronasGratisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_caronas_gratis, container, false);


        TextView tvHorarioChegadaLista = view.findViewById(R.id.tvHorarioChegadaLista);
        btnCompartilhar = view.findViewById(R.id.btnCompartilhar);
        layoutLost = view.findViewById(R.id.layoutLost);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        caronaAdapter = new CaronaAdapter(getContext());
        recyclerView.setAdapter(caronaAdapter);
        recyclerView.setLayoutManager(layoutManager);

        buscarCaronas();
        caronaAdapter.getListCaronas().clear();

        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "E ai, estou usando o App Caronas para pegar caronas grátis e/ou baratas, baixe também: " +
                        "https://drive.google.com/drive/folders/1ZPKojnhW1kYc_CdTByaR-TmoGJGL_DhX?usp=sharing");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        return view;
    }


    private void buscarCaronas() {
        FirebaseFirestore.getInstance().collection("/caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots, @Nullable final FirebaseFirestoreException e) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Carona car = doc.toObject(Carona.class);
                            if (car.getValorCarona().equals("0") && car.getQtdVagas() > 0) {
                                //comparar datas
                                Calendar dataHoje = Calendar.getInstance();
                                Calendar dataCarona = new GregorianCalendar();
                                String data = car.getData();
                                String dia = data.substring(0, 2);
                                String mes = data.substring(3, 5);
                                String ano = data.substring(6, 10);
                                dataCarona.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
                                dataCarona.set(Calendar.MONTH, Integer.parseInt(mes));
                                dataCarona.set(Calendar.YEAR, Integer.parseInt(ano));
                                //Pegar as caronas somente de hoje ou de dias seguintes
                                if (dataCarona.after(dataHoje) || dataCarona.equals(dataHoje)) {
                                    caronaAdapter.add(car);
                                    caronaAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        if (caronaAdapter.getListCaronas().size() == 0) {
                            layoutLost.setVisibility(View.VISIBLE);
                            btnCompartilhar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(v.getContext(), "Compartilhar", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }


}
