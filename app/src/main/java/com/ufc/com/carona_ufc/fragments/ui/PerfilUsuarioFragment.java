package com.ufc.com.carona_ufc.fragments.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilUsuarioFragment extends Fragment {
    private ActionBar bar;
    private Usuario usuario;
    private CaronaAdapter caronaAdapter;
    private CaronaAdapter caronaAdapter2;
    private TextView qtdOferecidas, qtdPegas;

    public PerfilUsuarioFragment(Usuario user, ActionBar bar) {
        this.usuario = user;
        this.bar = bar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_usuario, container, false);


    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bar.setTitle("Perfil");

        //relacionar os dados com o xml
        ImageView imgPerfilUsuario = view.findViewById(R.id.imgPerfilUsuario);
        TextView tvNomeUsuario = view.findViewById(R.id.tvNomePerfil);
        ImageView btnTrocarFotoPerfil = view.findViewById(R.id.btnTrocarFoto);
        TextView tvEnderecoUsuario = view.findViewById(R.id.tvEnderecoPerfil);
        TextView tvAvaliacaoUsuario = view.findViewById(R.id.tvAvaliacaoPerfil);
        TextView tvMiniBio = view.findViewById(R.id.tvMiniBioPerfil);
        ImageView btnTrocarFotoCapa = view.findViewById(R.id.btnFotoCapa);
        RecyclerView rvCaronasOfe = view.findViewById(R.id.rvCaronasOferecidasHorinzontal);
        RecyclerView rvCaronasPegas = view.findViewById(R.id.rvCaronasPegasHorizontal);
        qtdOferecidas = view.findViewById(R.id.inputQtdOferecidas);
        qtdPegas = view.findViewById(R.id.inputQtdPegas);
        qtdOferecidas.setTextSize(15);
        qtdPegas.setTextSize(15);

        //setar os dados
        Picasso.get().load(usuario.getUrlFotoUser()).into(imgPerfilUsuario);
        tvNomeUsuario.setText(usuario.getNomeUser());
        if (usuario.getEnderecoUser().equals("")) {
            tvEnderecoUsuario.setText("Endereço não informado");
        } else {
            tvEnderecoUsuario.setText(usuario.getEnderecoUser());
        }
        tvAvaliacaoUsuario.setText("Avaliação do motorista: " + usuario.getAvaliacao());
        if (usuario.getMiniBiografiaUser() == null || usuario.getMiniBiografiaUser().equals("")) {
            tvMiniBio.setVisibility(View.GONE);
            //Colocar um editor de texto aqui com gone no layout
        } else {
            tvMiniBio.setText(usuario.getMiniBiografiaUser());
        }

        //Instanciar o Linear Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setReverseLayout(false);

        //Popular Caronas Oferecidas
        //Agora o adapter
        caronaAdapter = new CaronaAdapter(getContext());
        //Setar o adapter no recyclerview
        rvCaronasOfe.setAdapter(caronaAdapter);
        //setar o layoutManager no rv
        rvCaronasOfe.setLayoutManager(layoutManager);
        //Fazer uma busca em todas as caronas e comparar o id da carona com meu user.getId();
        buscarCaronasOferecidas(usuario.getIdUser());


        //Popular caronas pegas
        caronaAdapter2 = new CaronaAdapter(getContext());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setReverseLayout(false);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rvCaronasPegas.setLayoutManager(layoutManager1);
        rvCaronasPegas.setAdapter(caronaAdapter2);
        buscarCaronasPegas(usuario.getIdUser());


        btnTrocarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Trocar de foto do perfil", Toast.LENGTH_SHORT).show();
            }
        });
        btnTrocarFotoCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Trocar foto da capa", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void buscarCaronasPegas(final String idUser) {
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
                                                        caronaAdapter2.add(carona);
                                                        caronaAdapter2.notifyDataSetChanged();
                                                    }
                                                }
                                                qtdPegas.setText("Caronas Pegas: " + caronaAdapter2.getListCaronas().size());
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void buscarCaronasOferecidas(final String idUser) {
        FirebaseFirestore.getInstance().collection("caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Carona carona = doc.toObject(Carona.class);
                            if (carona.getIdMotorista().equals(idUser)) {
                                caronaAdapter.add(carona);
                                caronaAdapter.notifyDataSetChanged();
                            }
                        }
                        qtdOferecidas.setText("Caronas Oferecidas: " + caronaAdapter.getListCaronas().size());
                    }
                });
    }


}
