package com.ufc.com.carona_ufc.views.fragments.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.controller.adapters.CaronaAdapter;
import com.ufc.com.carona_ufc.model.Carona;
import com.ufc.com.carona_ufc.model.CaronaPega;

public class HistoricoCaronasFragment extends Fragment {
    RecyclerView rvHistoricoCaronas;
    CaronaAdapter caronaAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historico_caronas, container, false);


        caronaAdapter = new CaronaAdapter(getContext());
        rvHistoricoCaronas = root.findViewById(R.id.rvCaronasHistorico);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);
        rvHistoricoCaronas.setAdapter(caronaAdapter);
        rvHistoricoCaronas.setLayoutManager(layoutManager);

        buscarCaronasHistorico(FirebaseAuth.getInstance().getUid());


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
    }

    private void buscarCaronasHistorico(final String idUser) {
        FirebaseFirestore.getInstance().collection("caronasPegas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            final CaronaPega caronaPega = doc.toObject(CaronaPega.class);
                            if (caronaPega.getIdUser().equals(idUser)) {
                                FirebaseFirestore.getInstance().collection("caronas")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                                    Carona carona = doc.toObject(Carona.class);
                                                    if (carona.getId().equals(caronaPega.getIdCarona())) {
                                                        caronaAdapter.add(carona);
                                                        caronaAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

}