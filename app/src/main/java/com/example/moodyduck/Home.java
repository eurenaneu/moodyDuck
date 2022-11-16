package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity {
    FloatingActionButton fabio, fabHoje, fabOutro, fabOntem;
    Animation fabOpen, fabClose, fabUp, fabDown;
    Calendar c = Calendar.getInstance();
    ArrayList<Registros> registros = new ArrayList<>();
    boolean estaAberto = false;
    BottomNavigationView bnv;
    Adaptador adaptador;
    RecyclerView rv;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_home_backup);
        bnv = findViewById(R.id.bottom_nav);
        fabio = findViewById(R.id.fab);
        fabOntem = findViewById(R.id.fabOntem);
        fabHoje = findViewById(R.id.fabHoje);
        fabOutro = findViewById(R.id.fabOutro);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        setupHistorico();

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

        fabOnClicks();

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
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void fabOnClicks() {
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
                AddRegistro.tData = "Hoje";
                startActivity(new Intent(Home.this, AddRegistro.class));
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

    public void setupHistorico(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Users").child(user.getUid()).child("Registros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try {
                        int setImg = 0;
                        String data = dataSnapshot.child("data").getValue().toString();
                        String horario = dataSnapshot.child("horario").getValue().toString();
                        String humor = dataSnapshot.child("nome").getValue().toString();

                        if(humor.equals("feliz")) {
                            setImg = 3;
                        } else if(humor.equals("neutro")){
                            setImg = 2;
                        } else if(humor.equals("triste")){
                            setImg = 1;
                        }

                        Registros r = new Registros(humor.toUpperCase(), data.replace(".","/")+" - "+horario, setImg);
                        registros.add(r);
                        adaptador = new Adaptador(getApplicationContext(), registros);
                        rv.setAdapter(adaptador);
                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adaptador.notify();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}