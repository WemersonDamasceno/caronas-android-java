package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
    ProgressDialog progressCriar;
    ProgressDialog progressCriar2;

    double latOri, latDest, lngOri, lngDest;


    @SuppressLint("SetTextI18n")
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
        progressCriar = new ProgressDialog(this);
        progressCriar2 = new ProgressDialog(this);


        //autoComplete dos enderecos
        etLocalSaida.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));
        etLocalChegada.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));


        //pegar a localização da pessoa;
        ivUseMyLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                progressCriar.setTitle("Aguarde um momento...");
                progressCriar.setMessage("Verificando sua localização");
                progressCriar.show();
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
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Atenção!");
                    alert.setMessage("Preencha todos os campos...");
                    alert.setPositiveButton("Ok", null);
                    alert.show();
                } else if (btnCriarCarona.getText().toString().equals("Criar Carona")
                        || btnCriarCarona.getText().toString().equalsIgnoreCase("Confirmar Edição")) {
                    progressCriar2.setTitle("Aguarde um momento...");
                    progressCriar2.setMessage("Criando sua carona...");
                    progressCriar2.show();


                    pegarLatLngSaidaChegada(etLocalSaida.getText().toString(), etLocalChegada.getText().toString());

                    carona.setEnderecoSaida(etLocalSaida.getText().toString());
                    carona.setEnderecoChegada(etLocalChegada.getText().toString());
                    carona.setData(tvData.getText().toString());
                    carona.setHora(tvHora.getText().toString());
                    carona.setIdMotorista(FirebaseAuth.getInstance().getUid());
                    carona.setQtdVagas(Integer.parseInt(etVagas.getText().toString()));
                    carona.setCheckBoxHelp(checkBoxHelp.isChecked());
                    progressCriar.dismiss();
                    //abrir nova activity
                    Intent intent = new Intent(v.getContext(), ConfirmarCaronaActivity.class);
                    bundleLatLng.putParcelable("carona", carona);
                    intent.putExtra("bundle", bundleLatLng);
                    startActivity(intent);
                    finish();
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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String minuto = minute + "";
        if (minute < 10) {
            minuto = "0" + minute;
        }
        String hora = hourOfDay + "";
        if (hourOfDay < 10) {
            hora = "0" + hourOfDay;
        }
        carona.setHora(hora + ":" + minuto);
        bundleLatLng.putParcelable("dados", carona);
        tvHora.setText(hora + ":" + minuto);
    }

    @SuppressLint("SetTextI18n")
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
        progressCriar.dismiss();
    }

    //CONVERTER LATLNG EM UM ENDEREÇO
    private String getEnderecoWithLatLng(double latitude, double longitude) {
        String endereco = "Falha em obter endereço";
        Geocoder geocoder = new Geocoder(OferecerCaronaActivity.this, Locale.getDefault());
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