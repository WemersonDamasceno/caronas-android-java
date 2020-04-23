package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ufc.com.carona_ufc.R;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText senha;
    TextView tvEsqueciSenha;
    Button btnCriarConta;
    Button btnEntrar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));

        //Autentificacao com firebase
        mAuth = FirebaseAuth.getInstance();

        senha = findViewById(R.id.senhaLogin);
        email = findViewById(R.id.emailLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvEsqueciSenha = findViewById(R.id.tvEsqueceuASenha);


        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar se o usuario ja está no banco
                String email1 = email.getText().toString();
                String senha1 = senha.getText().toString();

                if (email1.equals("")) {
                    Toast.makeText(LoginActivity.this, "O email não pode ser vazio!", Toast.LENGTH_SHORT).show();
                }
                if (senha1.equals("")) {
                    Toast.makeText(LoginActivity.this, "A senha não pode ser vazio!", Toast.LENGTH_SHORT).show();
                } else {
                    fazerLogin(email1, senha1);
                }

            }
        });

        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecuperarSenhaUserActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fazerLogin(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("teste", "sucesso ao fazer login");
                            Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro ao fazer Login");
            }
        });
    }

}
