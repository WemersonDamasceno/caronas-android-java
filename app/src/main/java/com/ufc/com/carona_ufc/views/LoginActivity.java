package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    EditText emailLogin;
    EditText senhaLogin;
    TextView tvEsqueciSenha;
    Button btnCriarConta;
    Button btnEntrar;
    FirebaseAuth mAuth;
    private ProgressBar circularBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));

        //Autentificacao com firebase
        mAuth = FirebaseAuth.getInstance();
        circularBar = findViewById(R.id.circularBar);
        senhaLogin = findViewById(R.id.senhaLogin);
        emailLogin = findViewById(R.id.emailLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvEsqueciSenha = findViewById(R.id.tvEsqueceuASenha);


        //esconder e com o GONE ele remove
        circularBar.setVisibility(View.INVISIBLE);

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CriarContaActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularBar.setVisibility(View.VISIBLE);
                //Verificar se o usuario ja está no banco
                String email1 = emailLogin.getText().toString();
                String senha1 = senhaLogin.getText().toString();

                if (email1.equals("")) {
                    emailLogin.setError("O campo de email é obrigatório!");
                }
                if (senha1.equals("")) {
                    senhaLogin.setError("O campo de senha é obrigatório!");
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

    private void fazerLogin(final String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            circularBar.setVisibility(View.GONE);
                            Log.i("teste", "sucesso ao fazer login");
                            Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro ao fazer Login: " + e.getMessage());
                if (e.getMessage().equals("The password is invalid or the user does not have a password.")) {
                    senhaLogin.setError("Senha incorreta");
                }
                if (e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    emailLogin.setError("Não foi encontrado uma conta com esse email :( ");
                }
            }
        });
    }

}
