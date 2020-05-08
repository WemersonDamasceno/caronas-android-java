package com.ufc.com.carona_ufc.fragments.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.ufc.com.carona_ufc.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.models.Carona;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaronasGratisFragment extends Fragment {

    public CaronasGratisFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    CaronaAdapter caronaAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caronas_gratis, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvHorarioChegadaLista = view.findViewById(R.id.tvHorarioChegadaLista);
        Button btnCompartilhar = view.findViewById(R.id.btnCompartilhar);
        LinearLayout layoutLost = view.findViewById(R.id.layoutLost);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);


        caronaAdapter = new CaronaAdapter();
        recyclerView.setAdapter(caronaAdapter);
        recyclerView.setLayoutManager(layoutManager);

        buscarCaronas();

        /*
        if (adapter.getItemCount() == 0 || adapter == null) {
            layoutLost.setVisibility(View.INVISIBLE);
            btnCompartilhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Compartilhar", Toast.LENGTH_SHORT).show();
                }
            });


        }
        */



        /*adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Carona carona = adapter.getListCaronas().get(position);
                Toast.makeText(getContext(), carona.getIdMotorista(), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), PegarCaronaActivity.class);
                intent1.putExtra("carona", carona);
                startActivity(intent1);
            }
        });*/


    }

    private void buscarCaronas() {
        FirebaseFirestore.getInstance().collection("/caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Carona car = doc.toObject(Carona.class);
                            Log.i("rua", car.getEnderecoSaida());
                            caronaAdapter.add(car);
                            caronaAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}
