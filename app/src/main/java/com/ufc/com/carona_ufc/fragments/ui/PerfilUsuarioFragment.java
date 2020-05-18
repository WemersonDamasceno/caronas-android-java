package com.ufc.com.carona_ufc.fragments.ui;


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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.models.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilUsuarioFragment extends Fragment {
    ActionBar bar;
    private Usuario usuario;
    RecyclerView rvCaronasOferecidasHorinzontal;
    RecyclerView rvCaronasPegas;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCaronasOferecidasHorinzontal = view.findViewById(R.id.rvCaronasOferecidasHorinzontal);
        rvCaronasPegas = view.findViewById(R.id.rvCaronasPegasHorizontal);


        bar.setTitle("Perfil");
        //relacionar os dados com o xml
        ImageView imgPerfilUsuario = view.findViewById(R.id.imgPerfilUsuario);
        TextView tvNomeUsuario = view.findViewById(R.id.tvNomeUsuario);
        ImageView btnTrocarFoto = view.findViewById(R.id.btnTrocarFoto);
        TextView tvEnderecoUsuario = view.findViewById(R.id.tvEnderecoUsuario);

        //setar os dados
        Picasso.get().load(usuario.getUrlFotoUser()).into(imgPerfilUsuario);
        tvNomeUsuario.setText(usuario.getNomeUser());
        tvEnderecoUsuario.setText(usuario.getEnderecoUser());


        btnTrocarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Trocar de foto", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
