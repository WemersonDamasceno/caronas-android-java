package com.ufc.com.carona_ufc.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.PlaceAutoSuggestAdapter;
import com.ufc.com.carona_ufc.fragments.DatePickerFragment;
import com.ufc.com.carona_ufc.fragments.TimePickerFragment;

public class OferecerCaronaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Button btnCriarCarona;

    AutoCompleteTextView etLocalSaida;
    AutoCompleteTextView etLocalChegada;

    ImageView ivRelogio;
    ImageView ivData;
    TextView tvHora;
    ImageView ivUseMyLocation;
    TextView tvData;
    EditText etVagas;
    //teste
    TextView tvSaida;
    TextView tvChegada;
    //fim teste


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferecer_carona);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));

        etLocalSaida = findViewById(R.id.localSaida);
        etLocalChegada = findViewById(R.id.localDeChegada);
        btnCriarCarona = findViewById(R.id.btnCriarCarona);
        ivRelogio = findViewById(R.id.ivRelogioCarona);
        ivData = findViewById(R.id.ivDataCarona);
        tvData = findViewById(R.id.tv_Data);
        tvHora = findViewById(R.id.tv_Hora);
        etVagas = findViewById(R.id.etVagas);
        ivUseMyLocation = findViewById(R.id.ivUseMyLocation);

        tvSaida = findViewById(R.id.tvSaida);
        tvChegada = findViewById(R.id.tvChegada);


        etLocalSaida.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));
        etLocalChegada.setAdapter(new PlaceAutoSuggestAdapter(OferecerCaronaActivity.this, android.R.layout.simple_list_item_1));


        btnCriarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLocalSaida.getText().toString().equals("")
                        || etLocalChegada.getText().toString().equals("")
                        || tvHora.getVisibility() == View.INVISIBLE
                        || tvData.getVisibility() == View.INVISIBLE
                        || etVagas.getText().toString().equals("")) {
                    Toast.makeText(OferecerCaronaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    //teste
                    pegarLatLngSaidaChegada();
                } else {

                    Toast.makeText(OferecerCaronaActivity.this, "Carona Criada Com Sucesso", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        //pegar a localização da pessoa;
        ivUseMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checarPermissaoClient();
            }
        });

        ivRelogio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "hora");
                tvHora.setVisibility(View.VISIBLE);
            }
        });

        ivData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "data");
                tvData.setVisibility(View.VISIBLE);
            }
        });


    }

    private void pegarLatLngSaidaChegada() {

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String minuto = minute + "";
        if (minute < 10) {
            minuto = "0" + minute;
        }
        tvHora.setText("Hora: " + hourOfDay + ":" + minuto);
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
        tvData.setText("Data: " + dia + "/" + mes + "/" + year);
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
                // A permissão foi concedida. Pode continuar
                Toast.makeText(this, "Sucess | Permissão concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Fail | Aceite a permissão para usar o app", Toast.LENGTH_LONG).show();
                //O programa necessita disso
                checarPermissaoClient();
                // A permissão foi negada. Precisa ver o que deve ser desabilitado
            }
            return;
        }
    }


}
