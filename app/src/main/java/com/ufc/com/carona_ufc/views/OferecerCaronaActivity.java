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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.UUID;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferecer_carona);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));

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
                } else {

                    final ProgressButton progressButton = new ProgressButton(OferecerCaronaActivity.this);
                    progressButton.buttonAtivo();
                    pegarLatLngSaidaChegada();
                    Handler thread = new Handler();
                    thread.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressButton.buttonDesativo();
                        }
                    }, 2000);


                    carona.setEnderecoSaida(etLocalSaida.getText().toString());
                    carona.setEnderecoChegada(etLocalChegada.getText().toString());
                    carona.setData(tvData.getText().toString());
                    carona.setHora(tvHora.getText().toString());
                    carona.setIdMotorista(FirebaseAuth.getInstance().getUid());
                    carona.setQtdVagas(1);
                    carona.setCheckBoxHelp(checkBoxHelp.isChecked());
                    carona.setId(UUID.randomUUID().toString());


                    //abrir nova activity
                    Intent intent = new Intent(v.getContext(), ConfirmarCaronaActivity.class);
                    bundleLatLng.putParcelable("carona", carona);
                    intent.putExtra("bundle", bundleLatLng);
                    startActivity(intent);
                }
            }
        });

        //pegar a localização da pessoa;
        ivUseMyLocation.setOnClickListener(new View.OnClickListener() {
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


    }

    //pegar a latlng dos endereços digitados
    private void pegarLatLngSaidaChegada() {
        Geocoder geocoder = new Geocoder(OferecerCaronaActivity.this, Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(etLocalSaida.getText().toString(), 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                double latOrigem = address.getLatitude();
                double lngOrigem = address.getLongitude();

                carona.setLatOrigem(latOrigem);
                carona.setLngOrigem(lngOrigem);
                bundleLatLng.putParcelable("dados", carona);
            }
            List addressList1 = geocoder.getFromLocationName(etLocalChegada.getText().toString(), 1);
            if (addressList1 != null && addressList1.size() > 0) {
                Address address1 = (Address) addressList1.get(0);
                double latDestino = address1.getLatitude();
                double lngDestino = address1.getLongitude();

                carona.setLatDestino(latDestino);
                carona.setLngDestino(lngDestino);
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
    //Faz a pergunta para o usuario da PERMISSAO
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