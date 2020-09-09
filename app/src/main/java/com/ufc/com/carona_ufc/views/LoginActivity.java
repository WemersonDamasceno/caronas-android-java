package com.ufc.com.carona_ufc.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.dao.UsuarioFirebaseDAO;
import com.ufc.com.carona_ufc.models.Usuario;

public class LoginActivity extends AppCompatActivity {
    EditText emailLogin;
    EditText senhaLogin;
    TextView btnCriarConta;
    Button btnEntrar;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ProgressDialog progressLoginGoogle;
    //Login com o Google
    SignInButton btnEntrarGoogle;
    GoogleSignInClient mGoogleSingInCliente;
    int RC_SING_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));

        //Autentificacao com firebase
        mAuth = FirebaseAuth.getInstance();
        senhaLogin = findViewById(R.id.senhaLogin);
        emailLogin = findViewById(R.id.emailLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrarGoogle = findViewById(R.id.btnEntrarGoogle);

        progressDialog = new ProgressDialog(this);
        progressLoginGoogle = new ProgressDialog(this);


        //configurar o login google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSingInCliente = GoogleSignIn.getClient(this, gso);

        btnEntrarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Aguarde um momento...");
                progressDialog.setMessage("Conferindo seus dados...");
                progressDialog.show();
                fazerLoginGoogle();
            }
        });


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
                //Verificar se o usuario ja está no banco
                String email1 = emailLogin.getText().toString();
                String senha1 = senhaLogin.getText().toString();

                if (email1.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Atenção!");
                    alert.setMessage("O campo de email é obrigatório!");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                    progressDialog.dismiss();
                    emailLogin.setError("O campo de email é obrigatório!");
                }
                if (senha1.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Atenção!");
                    alert.setMessage("O campo de senha é obrigatório!");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                    progressDialog.dismiss();
                    senhaLogin.setError("O campo de senha é obrigatório!");
                } else {
                    progressDialog.setTitle("Aguarde um momento...");
                    progressDialog.setMessage("Estamos conferindo seus dados...");
                    progressDialog.show();
                    fazerLogin(email1, senha1);
                }
            }
        });

    }

    private void fazerLoginGoogle() {
        Intent singInIntent = mGoogleSingInCliente.getSignInIntent();
        startActivityForResult(singInIntent, RC_SING_IN);
    }
    //resposta do metodo


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSingInResult(task);
        }


    }

    private void handleSingInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            Log.i("teste", "erro no login google: " + e.getMessage());
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //criar novo usuario
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            criarNovoUser(user);
                        }
                    }
                });
    }

    private void criarNovoUser(FirebaseUser user) {
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acc != null) {
            //criar novo user no banco.
            String nome = acc.getDisplayName();
            String email = acc.getEmail();
            String id = FirebaseAuth.getInstance().getUid();
            Uri userFoto = acc.getPhotoUrl();

            Usuario usuario = new Usuario();
            usuario.setNomeUser(nome);
            usuario.setEmailUser(email);
            usuario.setUrlFotoUser(userFoto.toString());
            usuario.setIdUser(id);

            UsuarioFirebaseDAO usuarioFirebaseDAO = new UsuarioFirebaseDAO();
            usuarioFirebaseDAO.salvarUsuarioBanco(usuario);
            progressLoginGoogle.dismiss();
            startActivity(new Intent(this, PaginaInicialActivity.class));
        }
    }


    private void fazerLogin(final String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("teste", "sucesso ao fazer login");
                            Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "erro ao fazer Login: " + e.getMessage());
                if (e.getMessage().equals("The password is invalid or the user does not have a password.")) {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(getApplicationContext());
                    alert1.setTitle("Atenção!");
                    alert1.setMessage("Verifique sua senha e tente novamente");
                    alert1.setPositiveButton("Ok", null);
                    alert1.show();
                    progressDialog.dismiss();
                    senhaLogin.setError("Senha incorreta");
                }
                if (e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    progressDialog.dismiss();
                    emailLogin.setError("Não foi encontrado uma conta com esse email'");
                }
                if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                    alert.setMessage("Falha ao conectar-se com a internet!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //faz nada
                        }
                    });
                    alert.show();
                }
                if (e.getMessage().equals("The email address is badly formatted.")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                    alert.setMessage("O email está mal formatado, tente novamente!");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                }
            }
        });
    }

}
