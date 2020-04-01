package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ufc.com.carona_ufc.OferecerCaronaActivity;
import com.ufc.com.carona_ufc.R;


public class MainActivity extends AppCompatActivity {
Button btnOferecerCarona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7E5DCA")));

        btnOferecerCarona = findViewById(R.id.btnOferecerCarona);





        btnOferecerCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OferecerCaronaActivity.class);
                startActivity(intent);
            }
        });


    }
}
