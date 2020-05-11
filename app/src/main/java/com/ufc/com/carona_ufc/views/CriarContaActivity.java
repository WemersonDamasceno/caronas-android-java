package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskedittext.MaskEditText;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.dao.UsuarioFirebaseDAO;
import com.ufc.com.carona_ufc.models.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


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
    //Foto
    Uri selectedImage;
    //Usuario
    Usuario user;
    Uri urlFoto = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));

        imgEscolherFotoPerfil = findViewById(R.id.imgEscolherFoto);
        edNomeNovo = findViewById(R.id.edNomeNovo);
        edEnderecoNovo = findViewById(R.id.edEnderecoNovo);
        useMyLocation = findViewById(R.id.useMyLocation);
        edEmailNovo = findViewById(R.id.edEmail);
        edSenhaNovo = findViewById(R.id.edSenhaNovo);
        btEntrarNovo = findViewById(R.id.btEntrarNovo);
        edTelefoneNovo = findViewById(R.id.edTelefoneNovo);
        selectedImage = null;
        user = new Usuario();
        progressBar = findViewById(R.id.circularBarCriarConta);
        progressBar.setVisibility(View.GONE);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //checar permissao para a galeria
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CriarContaActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(CriarContaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }

        useMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checarPermissaoClient();
                //pegar a localização da pessoa
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //chegar permissão da internet
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
                progressBar.setVisibility(View.GONE);
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
                    if (email.equals(""))
                        edEmailNovo.setError("O campo email é obrigatório!");
                    if (senha.equals(""))
                        edSenhaNovo.setError("O campo email é obrigatório!");
                    if (nome.equals(""))
                        edNomeNovo.setError("O campo nome é obrigatório!");
                } else {
                    btEntrarNovo.setText(" Criando sua conta");
                    progressBar.setVisibility(View.VISIBLE);
                    creatNewUser(email, senha);
                }
            }
        });

    }

    private void uploadFoto() {
        if (selectedImage == null) {
            Toast.makeText(CriarContaActivity.this, "Não quer escolher uma foto ? Clique na imagem", Toast.LENGTH_LONG).show();
        }


        progressBar.setVisibility(View.VISIBLE);
        btEntrarNovo.setText("Criando sua conta");
        //salvar imagem no banco
        String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + fileName);
        ref.putFile(selectedImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressBar.setVisibility(View.GONE);
                                btEntrarNovo.setText("Criando sua conta");
                                //URL da minha imagem;
                                urlFoto = uri;
                                user.setUrlFotoUser(uri.toString());
                                //criar novo usuario
                                criarNovoUsuario();
                                Log.i("teste", "URL da imagem: " + uri.toString());
                                //abrir a tela inicial
                                Intent intent = new Intent(getBaseContext(), PaginaInicialActivity.class);
                                Log.i("teste", "sucesso da autentificacao, idUser: " + FirebaseAuth.getInstance().getUid());
                                progressBar.setVisibility(View.GONE);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("teste", "Falha ao fazer upload da foto: " + e.getMessage());
                        if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                            Toast.makeText(CriarContaActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Toast.makeText(CriarContaActivity.this, "Aguarde um pouco, " + (int) progress + "% completo", Toast.LENGTH_SHORT).show();
                btEntrarNovo.setText("Criando sua conta");
            }
        });

    }

    private void creatNewUser(String email, String senha) {
        //criar novo usuario
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (selectedImage != null) {
                                uploadFoto();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("teste", "erro na autentificacao: " + e.getMessage());
                        if (e.getMessage().equals("The email address is already in use by another account.")) {
                            edEmailNovo.setError("O email ja está sendo utilizado!");
                        }
                        if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                            edSenhaNovo.setError("A senha deve no minímo 6 caracteres");
                        }
                        if (e.getMessage().equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
                            Toast.makeText(CriarContaActivity.this, "Falha ao conectar-se com a internet", Toast.LENGTH_LONG).show();
                        }
                        if (e.getMessage().equals("The email address is badly formatted.")) {
                            Toast.makeText(CriarContaActivity.this, "O email esta em um formato válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void criarNovoUsuario() {
        //povoar o usuario
        user.setNomeUser(edNomeNovo.getText().toString());
        user.setEmailUser(edEmailNovo.getText().toString());
        user.setSenhaUser(edSenhaNovo.getText().toString());
        user.setIdUser(FirebaseAuth.getInstance().getUid());
        if (urlFoto != null) {
            user.setUrlFotoUser(urlFoto.toString());
        }

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

    //PERMISSAO DA GALERIA
    private boolean checarPermissaoGaleria() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CriarContaActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return true;
            } else {
                ActivityCompat.requestPermissions(CriarContaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
                return true;
            }
        }
        return false;
    }

    //PEGAR A FOTO DA GALERIA E COLOCAR NA IMAGEVIEW
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //acessar galeria
        if (resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS) {
            selectedImage = data.getData();
            Bitmap thumbnail;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgEscolherFotoPerfil.setImageBitmap(thumbnail);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                StringBuilder stringBuilder = new StringBuilder("");

                for (int i = 0; i <= returnAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");
                }
                endereco = stringBuilder.toString();
            } else {
                Toast.makeText(this, "Fail | Endereço não encontrado!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return endereco;
    }

}
