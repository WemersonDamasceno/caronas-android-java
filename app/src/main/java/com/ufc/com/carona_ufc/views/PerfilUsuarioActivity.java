package com.ufc.com.carona_ufc.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.models.Usuario;

public class PerfilUsuarioActivity extends AppCompatActivity {
    Usuario usuario;
    ImageView imgPerfilUsuario;
    TextView tvNomeUsuario;
    ImageView btnTrocarFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        final ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));

        usuario = getIntent().getExtras().getParcelable("user");
        //relacionar os dados com o xml
        imgPerfilUsuario = findViewById(R.id.imgPerfilUsuario);
        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        btnTrocarFoto = findViewById(R.id.btnTrocarFoto);


        //setar os dados
        Picasso.get().load(usuario.getUrlFotoUser()).into(imgPerfilUsuario);
        tvNomeUsuario.setText(usuario.getNomeUser());


        btnTrocarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PerfilUsuarioActivity.this, "Trocar de foto", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
