package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.adapters.ViewPagerAdapter;
import com.ufc.com.carona_ufc.models.Carona;

import java.util.ArrayList;

public class ProcurarCaronaActivity extends AppCompatActivity {
    static ArrayList<Carona> caronaArrayList;

    public static ArrayList<Carona> getListCaronas() {
        ArrayList<Carona> caronas = caronaArrayList;
        return caronas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_carona);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F1FAC")));
        ViewPager viewPager = findViewById(R.id.pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_money_off);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_money_on);


        if (getIntent() != null) {
            caronaArrayList = getIntent().getParcelableArrayListExtra("caronas");
        } else {
            //num sei
        }
    }

    //fazer o search view
    //Para o menu do SearchView
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ic_procurar) {
            Intent intent = new Intent(this, PesquisarCaronaActivity.class);
            //Utilizando animação
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
            ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_procurar, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
