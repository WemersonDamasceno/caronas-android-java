package com.ufc.com.carona_ufc.fragments.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.interfaces.ItemClickListener;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.views.PegarCaronaActivity;

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

        caronaAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Carona carona = caronaAdapter.getListCaronas().get(position);
                Toast.makeText(getContext(), "Verifique o endereço", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), PegarCaronaActivity.class);
                intent1.putExtra("carona", carona);
                FirebaseFirestore.getInstance().terminate().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                startActivity(intent1);

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
                            if (!car.isCheckBoxHelp() && car.getQtdVagas() > 0) {
                                caronaAdapter.add(car);
                                caronaAdapter.notifyDataSetChanged();
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
