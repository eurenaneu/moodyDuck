package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Stats extends AppCompatActivity {
    List<Entry> lineArrayList = new ArrayList<>();
    ArrayList<Integer> integerArrayList = new ArrayList<>();
    LineChart lineChart;
    int data;
    Timer timer = null;
    long tempo = 10000;
    Calendar c = Calendar.getInstance();

    //nav
    Animation fabOpen, fabClose, fabUp, fabDown;
    BottomNavigationView bnv;
    FloatingActionButton fabio, fabHoje, fabOutro, fabOntem;
    boolean estaAberto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_stats);
        lineChart = findViewById(R.id.linechart);
        lineChart.setNoDataText("Nenhum registro no momento :)");
        lineChart.setNoDataTextColor(Color.rgb(255,250,185));
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setBorderColor(Color.WHITE);
        lineChart.getLegend().setEnabled(false);
        visualizarDados();
        inicializarNav();
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
                    } catch (Exception e){

                    }
                }
            };
            timer.scheduleAtFixedRate(tarefa, tempo, tempo);
        }
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

    public void montaGrafico(){
        lineChart.clear();
        lineArrayList = new ArrayList<>();
        for(int i = 0; i < integerArrayList.size(); i++) {
            lineArrayList.add(new BarEntry(i, integerArrayList.get(i)));
        }
        LineDataSet lineDataset = new LineDataSet(lineArrayList, "");
        lineDataset.setColors(Color.WHITE);
        lineDataset.setValueTextSize(14);
        lineDataset.setValueTextColor(ContextCompat.getColor(this, R.color.amalero));
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "qckbold.ttf");
        lineDataset.setValueTypeface(tf);
        lineDataset.setDrawCircles(false);
        lineDataset.setColor(ContextCompat.getColor(this, R.color.amalero));
        LineData lineData = new LineData(lineDataset);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }
    public void visualizarDados(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("teste");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                integerArrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(int i = 0; i <= snapshot.getChildrenCount(); i++) {
                        data = Integer.parseInt(snapshot.getValue().toString());
                        integerArrayList.add(data);
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