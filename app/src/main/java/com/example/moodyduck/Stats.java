package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Stats extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    ArrayList<Integer> integerArrayList = new ArrayList<>();
    List<Entry> lineArrayList = new ArrayList<>();
    TextView tTitulo, tProximo, tAnterior;
    Calendar c = Calendar.getInstance();
    View bProximo, bAnterior;
    ProgressBar progressBar;
    LineChart lineChart;
    Timer timer = null;
    long tempo = 3000;
    RecyclerView rv;
    int qtd;

    //nav
    BottomNavigationView bnv;
    boolean estaAberto = false;
    Animation fabOpen, fabClose, fabUp, fabDown;
    FloatingActionButton fabio, fabHoje, fabOutro, fabOntem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_stats);
        progressBar = findViewById(R.id.progressBarChart);
        tAnterior = findViewById(R.id.txtAnterior);
        tTitulo = findViewById(R.id.txtGraphTitle);
        tProximo = findViewById(R.id.txtProximo);
        bAnterior = findViewById(R.id.bAnterior);
        bProximo = findViewById(R.id.bProximo);
        tTitulo.setText(nomeMes[c.get(Calendar.MONTH)]+", "+c.get(Calendar.YEAR));
        recyclerSetup();
        setupGrafico();
        visualizarDados();
        inicializarNav();
        fabOnClicks();

        bnv.setSelectedItemId(R.id.estatisticas);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.registros:
                        startActivity(new Intent(Stats.this, Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.estatisticas:
                        break;
                    case R.id.calendario:
                        startActivity(new Intent(Stats.this, Calendario.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.config:
                        startActivity(new Intent(Stats.this, Config.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        if(timer == null){
            timer = new Timer();
            TimerTask tarefa = new TimerTask() {
                @Override
                public void run() {
                    try {
                        montaGrafico();
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, tempo, tempo);
        }
    }

    public void fabOnClicks(){
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
                startActivity(new Intent(Stats.this, AddRegistro.class));
            }
        });

        fabHoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Hoje";
                startActivity(new Intent(Stats.this, AddRegistro.class));
            }
        });

        fabOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRegistro.tData = "Outro dia";
                startActivity(new Intent(Stats.this, AddRegistro.class));
            }
        });
    }

    public void setupGrafico(){
        lineChart = findViewById(R.id.linechart);
        lineChart.setNoDataText("");
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisLeft().setAxisMaximum(3f);
        lineChart.getAxisLeft().setAxisMinimum(1f);
        lineChart.getXAxis().setEnabled(true);
        lineChart.getXAxis().setDrawGridLines(true);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setBorderColor(Color.WHITE);
        lineChart.getLegend().setEnabled(false);
    }

    public void onBackPressed(){
        this.moveTaskToBack(true);
    }

    public void inicializarNav(){
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
    }

    public void mesProximo(View v){
        qtd++;
        visualizarDados();
        Toast.makeText(getApplicationContext(), "somou", Toast.LENGTH_SHORT).show();
    }

    public void mesAnterior(View v){
        qtd--;
        visualizarDados();
        Toast.makeText(getApplicationContext(), "diminuiu", Toast.LENGTH_SHORT).show();
    }

    public void montaGrafico(){
        lineChart.clear();
        lineArrayList = new ArrayList<>();
        for(int i = 0; i < integerArrayList.size(); i++) {
            lineArrayList.add(new BarEntry(i, integerArrayList.get(i)));
        }
        LineDataSet lineDataset = new LineDataSet(lineArrayList, "");
        lineDataset.setColors(Color.WHITE);
        lineDataset.setDrawValues(false);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "qckbold.ttf");
        lineDataset.setValueTypeface(tf);
        lineDataset.setDrawCircles(true);
        lineDataset.setCircleColor(ContextCompat.getColor(this, R.color.amalero));
        lineDataset.setColor(ContextCompat.getColor(this, R.color.amalero));
        LineData lineData = new LineData(lineDataset);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    public void recyclerSetup(){
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(user.getUid()).child("Objetivos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //continuar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void visualizarDados(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mes = nomeMes[c.get(Calendar.MONTH)+qtd];
        String ano = String.valueOf(c.get(Calendar.YEAR));

        Toast.makeText(getApplicationContext(), mes+" "+ano, Toast.LENGTH_SHORT).show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(ano).child(mes);
        ref.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                integerArrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String humor = dataSnapshot.child("humor").getValue().toString();
                    switch (humor) {
                        case "feliz":
                            integerArrayList.add(3);
                            break;
                        case "neutro":
                            integerArrayList.add(2);
                            break;
                        case "triste":
                            integerArrayList.add(1);
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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