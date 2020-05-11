package com.ufc.com.carona_ufc.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.fragments.ui.HistoricoCaronasFragment;
import com.ufc.com.carona_ufc.fragments.ui.PerfilUsuarioFragment;
import com.ufc.com.carona_ufc.fragments.ui.Pg_Inicial_Fragment;
import com.ufc.com.carona_ufc.fragments.ui.ToolsFragment;
import com.ufc.com.carona_ufc.models.Usuario;

import java.util.List;

public class PaginaInicialActivity extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    private ActionBarDrawerToggle drawerToggle;
    private AppBarConfiguration mAppBarConfiguration;
    //perfil drawer
    ImageView imgPerfilDrawer;
    TextView tvNomePerfilDrawer;
    TextView tvEmailPerfilDrawer;

    //imagem BD
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage fireBaseStorage;
    StorageReference storageReference;
    LinearLayout llMenuFoto;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pagina Inicial");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);
        imgPerfilDrawer = navigationView.getHeaderView(0).findViewById(R.id.imagePerfilMenu);
        tvEmailPerfilDrawer = navigationView.getHeaderView(0).findViewById(R.id.tvEmailPerfilDrawer);
        tvNomePerfilDrawer = navigationView.getHeaderView(0).findViewById(R.id.tvNomePerfilDrawer);
        llMenuFoto = navigationView.getHeaderView(0).findViewById(R.id.llMenuFoto);

        //primeira pagina a ser exibida
        Fragment fragment = new Pg_Inicial_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();


        final ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8E00CF")));

        //Verificar se o usuario já esta autenticado
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        //
        getUser(FirebaseAuth.getInstance().getUid());


        //abrir o perfil do usuario
        llMenuFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("/users")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    Usuario user = doc.toObject(Usuario.class);
                                    if (user.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                        setTitle("Perfil");
                                        Fragment fragment = new PerfilUsuarioFragment(user, bar);
                                        // Inserir o fragment no local dele na main activity
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                                        // Fechar o navigation drawer
                                        drawer.closeDrawers();
                                    }
                                }
                            }
                        });
            }
        });


    }


    private void getUser(final String uid) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.i("teste", "erro em buscar no bd: " + e.getMessage());
                        } else {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Usuario user = doc.toObject(Usuario.class);
                                if (user.getIdUser().equals(uid)) {
                                    Picasso.get().load(user.getUrlFotoUser()).into(imgPerfilDrawer);
                                    tvNomePerfilDrawer.setText(user.getNomeUser());
                                    tvEmailPerfilDrawer.setText(user.getEmailUser());
                                }
                            }
                        }
                    }
                });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass = Pg_Inicial_Fragment.class;
        switch (menuItem.getItemId()) {
            case R.id.nav_pagInicial:
                setTitle(menuItem.getTitle());
                fragmentClass = Pg_Inicial_Fragment.class;
                break;
            case R.id.nav_OferecerCarona:
                Intent intent = new Intent(this, OferecerCaronaActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_procurarcarona:
                Intent intent1 = new Intent(this, ProcurarCaronaActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_historicocarona:
                setTitle(menuItem.getTitle());
                fragmentClass = HistoricoCaronasFragment.class;
                break;
            case R.id.nav_tools:
                setTitle(menuItem.getTitle());
                fragmentClass = ToolsFragment.class;
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Compartilhar......", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_sair:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Enviar.......", Toast.LENGTH_SHORT).show();
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inserir o fragment no local dele na main activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        menuItem.setChecked(true);
        // Fechar o navigation drawer
        drawer.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }
}
