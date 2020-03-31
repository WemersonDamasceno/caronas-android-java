package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ufc.com.carona_ufc.R;

public class LoginActivity extends AppCompatActivity {
TextView criarConta;
Button btnEntrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criarConta = findViewById(R.id.txtNãoTemConta);
        btnEntrar = findViewById(R.id.btnEntrar);

        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
