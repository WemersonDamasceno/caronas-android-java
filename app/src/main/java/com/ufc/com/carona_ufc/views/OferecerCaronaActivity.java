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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.LocationListener;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.PlaceAutoSuggestAdapter;
import com.ufc.com.carona_ufc.fragments.DatePickerFragment;
import com.ufc.com.carona_ufc.fragments.TimePickerFragment;

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
    //teste
    TextView tvSaida;
    TextView tvChegada;
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
        bundleLatLng = new Bundle();

        tvSaida = findViewById(R.id.tvSaida);
        tvChegada = findViewById(R.id.tvChegada);

        //autoComplete dos enderecos
        etLocalSaida.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));
        etLocalChegada.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));

        //Criar carona e pegar a latlng para mostrar no mapa
        btnCriarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLocalSaida.getText().toString().equals("")
                        || etLocalChegada.getText().toString().equals("")
                        || tvHora.getVisibility() == View.INVISIBLE
                        || tvData.getVisibility() == View.INVISIBLE
                        || etVagas.getText().toString().equals("")) {
                    Toast.makeText(OferecerCaronaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

                } else {
                    //teste
                    pegarLatLngSaidaChegada();
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    bundleLatLng.putString("qtdVagas", etVagas.getText().toString());
                    bundleLatLng.putBoolean("check", checkBoxHelp.isChecked());
                    intent.putExtra("latlng", bundleLatLng);
                    intent.putExtra("origem", etLocalSaida.getText().toString());
                    intent.putExtra("destino", etLocalChegada.getText().toString());
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
                bundleLatLng.putDouble("latSaida", address.getLatitude());
                bundleLatLng.putDouble("lngSaida", address.getLongitude());
                bundleLatLng.putString("origem", etLocalSaida.getText().toString());
                tvSaida.setText("" + address.getLatitude() + "," + address.getLongitude());
            }
            List addressList1 = geocoder.getFromLocationName(etLocalChegada.getText().toString(), 1);
            if (addressList1 != null && addressList1.size() > 0) {
                Address address1 = (Address) addressList1.get(0);
                bundleLatLng.putDouble("latChegada", address1.getLatitude());
                bundleLatLng.putDouble("lngChegada", address1.getLongitude());
                bundleLatLng.putString("chegada", etLocalChegada.getText().toString());
                tvChegada.setText("" + address1.getLatitude() + ", " + address1.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String minuto = minute + "";
        if (minute < 10) {
            minuto = "0" + minute;
        }
        bundleLatLng.putString("hora", hourOfDay + ":" + minuto);
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
        bundleLatLng.putString("data", dia + "/" + mes + "/" + year);
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
        Toast.makeText(this, "Aceite a permissão para uma localização mais precisa !", Toast.LENGTH_LONG).show();
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
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        //armazenar as coordenadas para mostrar no mapa
        bundleLatLng.putDouble("latSaida", latitude);
        bundleLatLng.putDouble("lngSaida", longitude);
        //converter a LatLgn em endereço com o getEnderecoWithLatLng
        etLocalSaida.setText(getEnderecoWithLatLng(latitude, longitude));
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
}
