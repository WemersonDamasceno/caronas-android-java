package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.FirebaseAuth;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.PlaceAutoSuggestAdapter;
import com.ufc.com.carona_ufc.fragments.DatePickerFragment;
import com.ufc.com.carona_ufc.fragments.TimePickerFragment;
import com.ufc.com.carona_ufc.models.Carona;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OferecerCaronaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LocationListener {

    CheckBox checkBoxHelp;
    Button btnCriarCarona;
    //autoComplete dos endereços
    AutoCompleteTextView etLocalSaida;
    AutoCompleteTextView etLocalChegada;

    //Hora e Data
    ImageView ivRelogio;
    ImageView ivData;
    TextView tvHora;
    TextView tvData;
    //icone da mylocation
    ImageView ivUseMyLocation;
    //myLOCATION
    LocationManager locationManager;
    EditText etVagas;
    //Tranferir as LatLng para a proxima tela
    Bundle bundleLatLng;
    Carona carona;

    double latOri, latDest, lngOri, lngDest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferecer_carona);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));

        checkBoxHelp = findViewById(R.id.cbPaga);
        etLocalSaida = findViewById(R.id.localSaida);
        etLocalChegada = findViewById(R.id.localDeChegada);
        btnCriarCarona = findViewById(R.id.btnCriarCarona);
        ivRelogio = findViewById(R.id.ivRelogioCarona);
        ivData = findViewById(R.id.ivDataCarona);
        tvData = findViewById(R.id.tv_Data);
        tvHora = findViewById(R.id.tv_Hora);
        etVagas = findViewById(R.id.etVagas);
        ivUseMyLocation = findViewById(R.id.ivUseMyLocation);
        carona = new Carona();
        bundleLatLng = new Bundle();


        //autoComplete dos enderecos
        etLocalSaida.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));
        etLocalChegada.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));


        //pegar a localização da pessoa;
        ivUseMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //pegar a localização da pessoa
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //chegar permissão da internet
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
            }
        });
        //Relógio
        ivRelogio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "hora");
                tvHora.setVisibility(View.VISIBLE);
            }
        });
        //Data
        ivData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "data");
                tvData.setVisibility(View.VISIBLE);
            }
        });

        //Criar carona e pegar a latlng para mostrar no mapa
        btnCriarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLocalSaida.getText().toString().equals("")
                        || etLocalChegada.getText().toString().equals("")
                        || tvHora.getText().toString().equals("")
                        || tvData.getText().toString().equals("")
                        || etVagas.getText().toString().equals("")) {
                    Toast.makeText(OferecerCaronaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }


                if (btnCriarCarona.getText().toString().equals("Criar Carona") || btnCriarCarona.getText().toString().equalsIgnoreCase("Confirmar Edição")) {
                    final ProgressButton progressButton = new ProgressButton(OferecerCaronaActivity.this);
                    progressButton.buttonAtivo();

                    pegarLatLngSaidaChegada(etLocalSaida.getText().toString(), etLocalChegada.getText().toString());

                    Handler thread = new Handler();
                    thread.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressButton.buttonDesativo();
                        }
                    }, 2200);

                    carona.setEnderecoSaida(etLocalSaida.getText().toString());
                    carona.setEnderecoChegada(etLocalChegada.getText().toString());
                    carona.setData(tvData.getText().toString());
                    carona.setHora(tvHora.getText().toString());
                    carona.setIdMotorista(FirebaseAuth.getInstance().getUid());
                    carona.setQtdVagas(Integer.valueOf(etVagas.getText().toString()));
                    carona.setCheckBoxHelp(checkBoxHelp.isChecked());
                    //carona.setId(UUID.randomUUID().toString());

                    //abrir nova activity
                    Intent intent = new Intent(v.getContext(), ConfirmarCaronaActivity.class);
                    bundleLatLng.putParcelable("carona", carona);
                    intent.putExtra("bundle", bundleLatLng);
                    startActivity(intent);
                }


            }
        });

        if (getIntent().getParcelableExtra("editar") != null) {
            final Carona carona1 = getIntent().getExtras().getParcelable("editar");
            etLocalSaida.setText(carona1.getEnderecoSaida());
            etLocalChegada.setText(carona1.getEnderecoChegada());
            tvData.setText(carona1.getData());
            tvHora.setText(carona1.getHora());
            etVagas.setText(String.valueOf(carona1.getQtdVagas()));
            btnCriarCarona.setText("Confirmar Edição");
            Log.i("teste", "Oferecer carona activity ok!");
            carona = carona1;
        }
    }

    //Editar a carona
    /*private void editarCarona(final Carona carona1) {
        //Editar a carona
        FirebaseFirestore.getInstance().collection("/caronas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Carona c = doc.toObject(Carona.class);
                            if (c.getId().equals(carona1.getId())) {
                                updateInFirebase(doc);
                            }
                        }
                    }
                });
    }

    private void updateInFirebase(DocumentSnapshot doc) {
        String data, endChegada, endSaida, hora, horaProx, idMoto, qtdVagas;
        boolean check;
        data = tvData.getText().toString();
        hora = tvHora.getText().toString();
        endChegada = etLocalChegada.getText().toString();
        endSaida = etLocalSaida.getText().toString();
        qtdVagas = etVagas.getText().toString();
        check = checkBoxHelp.isChecked();

        pegarLatLngSaidaChegada(endSaida, endChegada);
        //Quando tento editar
        //um arquivo mais de uma vez, ele entra em um loop infinito editando e retrocendendo
        FirebaseFirestore.getInstance().collection("/caronas")
                .document(doc.getId())
                .update("checkBoxHelp", check,
                        "data", data,
                        "hora", hora,
                        "enderecoChegada", endChegada,
                        "enderecoSaida", endSaida,
                        "qtdVagas", Integer.parseInt(qtdVagas),
                        "latDestino", latDest,
                        "lngDestino", lngDest,
                        "latOrigem", latOri,
                        "lngOrigem", lngOri,
                        "horaChegadaprox", "N/A"


                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("teste", "Updated completed");
                startActivity(new Intent(getBaseContext(), ProcurarCaronaActivity.class));
                finish();
            }
        });
    }*/

    //pegar a latlng dos endereços digitados
    private void pegarLatLngSaidaChegada(String localSaida, String localChegada) {
        Geocoder geocoder = new Geocoder(OferecerCaronaActivity.this, Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(localSaida, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                latOri = address.getLatitude();
                lngOri = address.getLongitude();

                carona.setLatOrigem(latOri);
                carona.setLngOrigem(lngOri);
                bundleLatLng.putParcelable("dados", carona);
            }
            List addressList1 = geocoder.getFromLocationName(localChegada, 1);
            if (addressList1 != null && addressList1.size() > 0) {
                Address address1 = (Address) addressList1.get(0);
                latDest = address1.getLatitude();
                lngDest = address1.getLongitude();

                carona.setLatDestino(latDest);
                carona.setLngDestino(lngDest);
                bundleLatLng.putParcelable("dados", carona);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Pocessamento completado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String minuto = minute + "";
        if (minute < 10) {
            minuto = "0" + minute;
        }
        carona.setHora(hourOfDay + ":" + minuto);
        bundleLatLng.putParcelable("dados", carona);
        tvHora.setText(hourOfDay + ":" + minuto);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String mes = month + "";
        String dia = dayOfMonth + "";
        if (month < 10) {
            mes = "0" + month;
        }
        if (dayOfMonth < 10) {
            dia = "0" + dayOfMonth;
        }
        carona.setData(dia + "/" + mes + "/" + year);
        bundleLatLng.putParcelable("dados", carona);
        tvData.setText(dia + "/" + mes + "/" + year);
    }

    @Override
    public void onLocationChanged(Location location) {
        carona.setLatOrigem(location.getLatitude());
        carona.setLngOrigem(location.getLongitude());
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        //converter a LatLgn em endereço com o getEnderecoWithLatLng
        etLocalSaida.setText(getEnderecoWithLatLng(latitude, longitude));
        carona.setEnderecoSaida(etLocalSaida.getText().toString());
        bundleLatLng.putParcelable("dados", carona);
    }

    //CONVERTER LATLNG EM UM ENDEREÇO
    private String getEnderecoWithLatLng(double latitude, double longitude) {
        String endereco = "Falha";
        Geocoder geocoder = new Geocoder(OferecerCaronaActivity.this, Locale.getDefault());
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


    public class ProgressButton {
        private CardView cardView;

        public ProgressButton(Context context) {
            cardView = findViewById(R.id.cardView);
        }

        public void buttonAtivo() {
            cardView.setVisibility(View.VISIBLE);
            btnCriarCarona.setVisibility(View.INVISIBLE);
        }

        public void buttonDesativo() {
            cardView.setVisibility(View.INVISIBLE);
            btnCriarCarona.setVisibility(View.VISIBLE);
        }

    }

}