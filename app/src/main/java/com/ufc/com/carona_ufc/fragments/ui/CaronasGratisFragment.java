package com.ufc.com.carona_ufc.fragments.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.interfaces.ItemClickListener;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.views.ConfirmarCaronaActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaronasGratisFragment extends Fragment {
    LinearLayout layoutLost;
    Button btnCompartilhar;
    public CaronasGratisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caronas_gratis, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCompartilhar = view.findViewById(R.id.btnCompartilhar);
        layoutLost = view.findViewById(R.id.layoutLost);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<Carona> listCaronas = new ArrayList<>();

        LatLng latLng = new LatLng(-5.418525, -39.452424);
        Carona c2 = new Carona("11201", "Quixeramobim", "Fortaleza", "05/05/2020", "10:00", 3, true, "Manuela Bu.", latLng, latLng);
        Carona c3 = new Carona("25054", "Quixadá", "Paus Brancos", "05/05/2020", "8:00", 2, true, "Jose Maria", latLng, latLng);
        Carona c4 = new Carona("25032", "Praça do Leão", "UECE", "05/05/2020", "16:00", 4, true, "Osvaldo", latLng, latLng);
        Carona c5 = new Carona("250589", "Praça do Leão", "UFC", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c6 = new Carona("250577", "UFC", "UECE", "05/05/2020", "12:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c7 = new Carona("250588", "UFC", "Centro", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c8 = new Carona("250563", "UFC", "Rodoviária", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c9 = new Carona("250512", "UFC", "UECE", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c10 = new Carona("250500", "UFC", "UECE", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);
        Carona c11 = new Carona("250525", "UFC", "UECE", "05/05/2020", "10:00", 3, true, "Osvaldo", latLng, latLng);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getBundleExtra("dados");
        if (bundle != null) {
            //Pegar somente o endereço separado pela ','
            String ori = bundle.getString("origem");
            String[] origem = ori.split(",");
            String des = bundle.getString("destino");
            String[] destino = des.split(",");


            Carona c1 = new Carona("14725", origem[0], destino[0],
                    bundle.getString("hora"), bundle.getString("data"),
                    Integer.valueOf(bundle.getString("qtdVagas")), false, "Wemerson", latLng, latLng);
            listCaronas.add(c1);

        }
        listCaronas.add(c2);
        listCaronas.add(c3);
        listCaronas.add(c4);
        listCaronas.add(c5);
        listCaronas.add(c6);
        listCaronas.add(c7);
        listCaronas.add(c8);
        listCaronas.add(c9);
        listCaronas.add(c10);
        listCaronas.add(c11);

        if (listCaronas.size() == 0 || listCaronas == null) {
            layoutLost.setVisibility(View.VISIBLE);
            btnCompartilhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Compartilhar", Toast.LENGTH_SHORT).show();
                }
            });

        }


        CaronaAdapter adapter = new CaronaAdapter(listCaronas);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Carona carona = listCaronas.get(position);
                Toast.makeText(getContext(), carona.getIdMotorista(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), ConfirmarCaronaActivity.class);
                intent1.putExtra("carona", carona);
                startActivity(intent1);
            }
        });


    }


}
