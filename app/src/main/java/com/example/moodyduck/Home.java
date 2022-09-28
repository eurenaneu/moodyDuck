package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.components.Lazy;

import java.util.Calendar;

public class Home extends AppCompatActivity {
    Calendar c = Calendar.getInstance();
    TextView t;
    Animation fabOpen, fabClose, fabUp, fabDown;
    BottomNavigationView bnv;
    FloatingActionButton fabio, fabHoje, fabOutro, fabOntem;
    boolean estaAberto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_home);
        bnv = findViewById(R.id.bottom_nav);
        fabio = findViewById(R.id.fab);
        fabOntem = findViewById(R.id.fabOntem);
        fabHoje = findViewById(R.id.fabHoje);
        fabOutro = findViewById(R.id.fabOutro);

        //anims
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.anim_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.anim_close);
        fabUp = AnimationUtils.loadAnimation(this, R.anim.anim_cima);
        fabDown = AnimationUtils.loadAnimation(this, R.anim.anim_baixo);

        // setar mês
        t = findViewById(R.id.mesAno);
        String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
        t.setText(nomeMes[c.get(c.MONTH)]+" de "+c.get(c.YEAR));

        fabio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animFab();
            }
        });

        fabOntem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Ontem";
                startActivity(new Intent(Home.this, AddRegistro.class));
            }
        });

        fabHoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Hoje";
                startActivity(new Intent(Home.this, AddRegistro.class));
            }
        });

        fabOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Outro dia";
                startActivity(new Intent(Home.this, AddRegistro.class));
            }
        });

        bnv.setSelectedItemId(R.id.registros);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.registros:
                        return true;
                    case R.id.estatisticas:
                        startActivity(new Intent(Home.this, Stats.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.calendario:
                        startActivity(new Intent(Home.this, Calendario.class));
                        return true;
                    case R.id.config:
                        startActivity(new Intent(Home.this, Config.class));
                        return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja deslogar?").setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Home.this, MainActivity.class));
                finish();
            }
        });
    }

    public void animFab(){
        if (estaAberto){
            fabio.startAnimation(fabClose);
            fabOntem.startAnimation(fabDown);
            fabHoje.startAnimation(fabDown);
            fabOutro.startAnimation(fabDown);
            fabOntem.setClickable(false);
            fabHoje.setClickable(false);
            fabOutro.setClickable(false);
            estaAberto = false;
        } else {
            fabio.startAnimation(fabOpen);
            fabOntem.startAnimation(fabUp);
            fabHoje.startAnimation(fabUp);
            fabOutro.startAnimation(fabUp);
            fabOntem.setClickable(true);
            fabHoje.setClickable(true);
            fabOutro.setClickable(true);
            estaAberto = true;
        }
    }
}