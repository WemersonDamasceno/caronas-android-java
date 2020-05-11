package com.ufc.com.carona_ufc.fragments.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.views.OferecerCaronaActivity;

public class Pg_Inicial_Fragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pg_inicial, container, false);

        Button btnOferecerCarona = root.findViewById(R.id.btnOferecerCarona);
        Button btnProcurarCarona = root.findViewById(R.id.btnProcurarCarona);

        btnOferecerCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OferecerCaronaActivity.class);
                //abrir activity com animação
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getContext(), R.anim.fade_in, R.anim.fade_out);
                ActivityCompat.startActivity(getContext(), intent, activityOptionsCompat.toBundle());
            }
        });
        btnProcurarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProcurarCaronaFragment();
                // Inserir o fragment no local dele na main activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });


        return root;
    }
}