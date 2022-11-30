package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class TelaObjetivos extends AppCompatActivity {
    Button voltar;
    RecyclerView rv;
    Adaptador adaptador;
    FloatingActionButton fabio, fabHoje, fabOutro, fabOntem;
    Animation fabOpen, fabClose, fabUp, fabDown;
    boolean estaAberto = false;
    BottomNavigationView bnv;
    ArrayList<Objetivos> objetivos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_tela_objetivos);
        voltar = findViewById(R.id.bVoltar);
        bnv = findViewById(R.id.bottom_nav);
        fabio = findViewById(R.id.fab);
        fabOntem = findViewById(R.id.fabOntem);
        fabHoje = findViewById(R.id.fabHoje);
        fabOutro = findViewById(R.id.fabOutro);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        setupRecycler();
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        fabio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animFab();
            }
        });

        fabOnClicks();

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.anim_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.anim_close);
        fabUp = AnimationUtils.loadAnimation(this, R.anim.anim_cima);
        fabDown = AnimationUtils.loadAnimation(this, R.anim.anim_baixo);

        bnv.setSelectedItemId(R.id.objetivos);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.registros:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.estatisticas:
                        startActivity(new Intent(getApplicationContext(), Stats.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.objetivos:
                        return true;
                    case R.id.config:
                        startActivity(new Intent(getApplicationContext(), Config.class));
                        return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        this.moveTaskToBack(true);
    }

    private void fabOnClicks() {
        fabOntem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Ontem";
                startActivity(new Intent(getApplicationContext(), AddRegistro.class));
            }
        });

        fabHoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Hoje";
                startActivity(new Intent(getApplicationContext(), AddRegistro.class));
            }
        });

        fabOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Outro dia";
                startActivity(new Intent(getApplicationContext(), AddRegistro.class));
            }
        });
    }

    private void animFab(){
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

    private void setupRecycler() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(user.getUid()).child("Objetivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                objetivos = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        String nome = dataSnapshot.child("nome").getValue().toString();
                        Objetivos o = new Objetivos(nome);
                        objetivos.add(o);
                        adaptador = new Adaptador(getApplicationContext(), objetivos, 3);
                        rv.setAdapter(adaptador);
                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adaptador.notify();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void atualizarObjetivos(View view){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Objetivos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    for(int i = 0; i < adaptador.getSelected().size(); i++){
                        String nomeObjetivo = adaptador.getSelected().get(i).getNome();
                        ref.child(nomeObjetivo).child("checked").setValue(true);
                    }

                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void voltar(){
        startActivity(new Intent(this, Home.class));
        this.finish();
    }

}