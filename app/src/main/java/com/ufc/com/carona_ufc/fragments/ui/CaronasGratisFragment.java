package com.ufc.com.carona_ufc.fragments.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private View view;
    public CaronasGratisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_caronas_gratis, container, false);

        Log.i("teste", "onCreateView");

        TextView tvHorarioChegadaLista = view.findViewById(R.id.tvHorarioChegadaLista);
        Button btnCompartilhar = view.findViewById(R.id.btnCompartilhar);
        LinearLayout layoutLost = view.findViewById(R.id.layoutLost);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);

        caronaAdapter = new CaronaAdapter();
        recyclerView.setAdapter(caronaAdapter);
        recyclerView.setLayoutManager(layoutManager);
        buscarCaronas();


        if (caronaAdapter.getItemCount() == 0 || caronaAdapter == null || caronaAdapter.getListCaronas().size() == 0) {
            layoutLost.setVisibility(View.INVISIBLE);
            btnCompartilhar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Compartilhar", Toast.LENGTH_SHORT).show();
                }
            });
        }


        caronaAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Carona carona = caronaAdapter.getListCaronas().get(position);
                Toast.makeText(getContext(), "Verifique o endereço", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), PegarCaronaActivity.class);
                intent1.putExtra("carona", carona);
                startActivity(intent1);
            }
        });
        return view;
    }


    private void buscarCaronas() {
        FirebaseFirestore.getInstance().collection("/caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.i("teste", e.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            Carona car = doc.toObject(Carona.class);
                            if (!car.isCheckBoxHelp()) {
                                caronaAdapter.add(car);
                                caronaAdapter.notifyDataSetChanged();
                            }

                        }

                    }
                });
    }
}
