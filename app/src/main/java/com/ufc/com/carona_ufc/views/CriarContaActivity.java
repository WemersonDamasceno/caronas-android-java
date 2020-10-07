package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.santalu.maskedittext.MaskEditText;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.controller.dao.UsuarioFirebaseDAO;
import com.ufc.com.carona_ufc.model.Usuario;

import java.util.List;
import java.util.Locale;


public class CriarContaActivity extends AppCompatActivity implements LocationListener {
    final int GALERIA_IMAGENS = 1;
    final int PERMISSAO_REQUEST = 2;
    EditText edNomeNovo;
    EditText edEnderecoNovo;
    ImageView useMyLocation;
    EditText edEmailNovo;
    EditText edSenhaNovo;
    Button btEntrarNovo;
    MaskEditText edTelefoneNovo;
    //myLOCATION
    LocationManager locationManager;
    //Galeria
    ImageView imgEscolherFotoPerfil;
    //Autentificacao
    private FirebaseAuth mAuth;
    //Usuario
    Usuario user;
    ProgressDialog progressDialog;
    String urlFoto = "https://firebasestorage.googleapis.com/v0/b/caronas-ufc.appspot.com/o/" +
            "images%2Fperson_location.png?alt=media&token=e698debb-213c-48f0-aa0d-6018a022fc6c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));

        imgEscolherFotoPerfil = findViewById(R.id.imgEscolherFoto);
        edNomeNovo = findViewById(R.id.edNomeNovo);
        edEnderecoNovo = findViewById(R.id.edEnderecoNovo);
        useMyLocation = findViewById(R.id.useMyLocation);
        edEmailNovo = findViewById(R.id.edEmail);
        edSenhaNovo = findViewById(R.id.edSenhaNovo);
        btEntrarNovo = findViewById(R.id.btEntrarNovo);
        edTelefoneNovo = findViewById(R.id.edTelefoneNovo);
        progressDialog = new ProgressDialog(this);
        user = new Usuario();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        useMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checarPermissaoClient();
                //pegar a localização da pessoa
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //chegar permissão da internet
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
            }
        });

        imgEscolherFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pegar foto da galeria
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA_IMAGENS);

            }
        });

        btEntrarNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmailNovo.getText().toString();
                String senha = edSenhaNovo.getText().toString();
                String nome = edNomeNovo.getText().toString();

                if (email.equals("") || senha.equals("") || nome.equals("")) {
                    if (email.equals("")) {
                        edEmailNovo.setError("O campo email é obrigatório!");
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle("Atenção!");
                        alert.setMessage("O campo email é obrigatório");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    }
                    if (senha.equals("")) {
                        edSenhaNovo.setError("O campo email é obrigatório!");
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle("Atenção!");
                        alert.setMessage("O campo senha é obrigatório");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //faz nada
                            }
                        });
                        alert.show();
                    }
                    if (nome.equals("")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle("Atenção!");
                        alert.setMessage("O campo nome é obrigatório");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //faz nada
                            }
                        });
                        alert.show();
                        edNomeNovo.setError("O campo nome é obrigatório!");
                    }
                } else {
                    progressDialog.setTitle("Aguarde um instante...");
                    progressDialog.setMessage("Estamos criando sua conta.");
                    progressDialog.show();
                    creatNewUser(email, senha);
                }
            }
        });

    }

    private void creatNewUser(String email, String senha) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Atenção!");
        //criar novo usuario
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            criarNovoUsuario();
                            //abrir a tela inicial
                            Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                            Log.i("teste", "sucesso da autentificacao, idUser: " + FirebaseAuth.getInstance().getUid());
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i("teste", "erro na autentificacao: " + e.getMessage());
                        if (e.getMessage().equals("The email address is already in use by another account.")) {
                            alert.setMessage("O email ja está sendo utilizado!");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //faz nada
                                }
                            });
                            alert.show();
                        }
                        if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                            alert.setMessage("A senha deve no minímo 6 caracteres");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //faz nada
                                }
                            });
                            alert.show();
                        }
                        if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                            Toast.makeText(CriarContaActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(CriarContaActivity.this, "O email esta em um formato válido", Toast.LENGTH_SHORT).show();
                            alert.setMessage("O email está mal formatado, tente novamente!");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //faz nada
                                }
                            });
                            alert.show();
                        }
                    }
                });
    }

    private void criarNovoUsuario() {
        //povoar o usuario
        user.setNomeUser(edNomeNovo.getText().toString());
        user.setEmailUser(edEmailNovo.getText().toString());
        //sem salvar a senha '-'
        user.setIdUser(FirebaseAuth.getInstance().getUid());
        user.setUrlFotoUser(urlFoto);

        if (!edEnderecoNovo.getText().equals("")) {
            user.setEnderecoUser(edEnderecoNovo.getText().toString());
        }
        if (!edTelefoneNovo.getText().equals("")) {
            user.setTelefoneUser(edTelefoneNovo.getText().toString());
        }
        //salvar o usuario no banco de dados
        UsuarioFirebaseDAO usuarioFirebaseDAO = new UsuarioFirebaseDAO();
        usuarioFirebaseDAO.salvarUsuarioBanco(user);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        //converter a LatLgn em endereço com o getEnderecoWithLatLng
        edEnderecoNovo.setText(getEnderecoWithLatLng(latitude, longitude));
    }

    //Faz a pergunta para o usuario da PERMISSAO SOBRE A LOCALIZAÇÃO
    private void checarPermissaoClient() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
        }
    }

    //O código abaixo faz o tratamento da resposta do usuário sobre a PERMISSAO
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Sucess | Permissão concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Fail | Aceite a permissão para usar sua localização!", Toast.LENGTH_LONG).show();
                // A permissão foi negada. Precisa ver o que deve ser desabilitado
            }
            return;
        }
    }

    //CONVERTER LATLNG EM UM ENDEREÇO
    private String getEnderecoWithLatLng(double latitude, double longitude) {
        String endereco = "Falha";
        Geocoder geocoder = new Geocoder(CriarContaActivity.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null) {
                Address returnAddress = addressList.get(0);
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i <= returnAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");
                }
                endereco = stringBuilder.toString();
            } else {
                Toast.makeText(this, "Fail | Endereço não encontrado!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return endereco;
    }

}
