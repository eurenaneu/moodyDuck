package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.TextView;
import android.widget.Toast;


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
    List<Entry> lineArrayList = new ArrayList<>();
    ArrayList<Integer> integerArrayList = new ArrayList<>();
    TextView tTitulo, tProximo, tAnterior;
    View bProximo, bAnterior;
    LineChart lineChart;
    int p, r;
    Timer timer = null;
    long tempo = 3000;
    Calendar c = Calendar.getInstance();

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
        tProximo = findViewById(R.id.txtProximo);
        tAnterior = findViewById(R.id.txtAnterior);
        tTitulo = findViewById(R.id.txtGraphTitle);
        bAnterior = findViewById(R.id.bBack);
        bProximo = findViewById(R.id.bNext);
        tTitulo.setText(nomeMes[c.get(Calendar.MONTH)]+", "+c.get(Calendar.YEAR));
        setupGrafico();
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

    public void setupGrafico(){
        lineChart = findViewById(R.id.linechart);
        lineChart.setNoDataText("");
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
    }

    public void onBackPressed(){
        startActivity(new Intent(this, Home.class));
        finish();
        overridePendingTransition(0, 0);
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

    public void avançarMes(View v){
        p++;
        visualizarDados();
        Toast.makeText(getApplicationContext(), "somou", Toast.LENGTH_SHORT).show();
    }

    public void voltarMes(View v){
        p--;
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

    public void visualizarDados(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mes = "";
        //ano menor que o atual
        if(c.get(Calendar.YEAR)-r != c.get(Calendar.YEAR)){
            try {
                mes = nomeMes[11 + p];
                if (mes.equals("janeiro")) {
                    tAnterior.setText("dezembro");
                } else {
                    tAnterior.setText(nomeMes[11 + p - 1]);
                }

                if(mes.equals("dezembro")){
                    tProximo.setText("janeiro");
                } else {
                    tProximo.setText(nomeMes[11 + p + 1]);
                }
            } catch (ArrayIndexOutOfBoundsException e){
                r++;
                p = 0;
                mes = nomeMes[11 + p];
                if(mes.equals("janeiro")){
                    tAnterior.setText("dezembro");
                } else {
                    tAnterior.setText(nomeMes[11 + p - 1]);
                }

                if(mes.equals("dezembro")){
                    tProximo.setText("janeiro");
                } else {
                    tProximo.setText(nomeMes[11 + p + 1]);
                }
            }
            // ano atual
        } else {
            try {
                mes = nomeMes[c.get(Calendar.MONTH) + p];
                if(nomeMes[c.get(Calendar.MONTH)+p].equals("janeiro")){
                    tAnterior.setText("dezembro");
                } else {
                    tAnterior.setText(nomeMes[c.get(Calendar.MONTH)+p-1]);
                }

                if(mes.equals("dezembro")){
                    tProximo.setText("janeiro");
                } else if(mes.equals(nomeMes[c.get(Calendar.MONTH)])) {
                    tProximo.setText("mês atual");
                } else {
                    tProximo.setText(nomeMes[c.get(Calendar.MONTH)+p+1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                r++;
                p = 0;
                mes = nomeMes[11+p];
                if(nomeMes[11+p].equals("janeiro")){
                    tAnterior.setText("dezembro");
                } else {
                    tAnterior.setText(nomeMes[11 + p - 1]);
                }

                if(mes.equals("dezembro")){
                    tProximo.setText("janeiro");
                } else if(mes.equals(nomeMes[c.get(Calendar.MONTH)])) {
                    tProximo.setText("mês atual");
                } else {
                    tProximo.setText(nomeMes[11 + p + 1]);
                }
            }
        }
        String ano = String.valueOf(c.get(Calendar.YEAR)-r);
        tTitulo.setText(mes+", "+ano);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(ano).child(mes);

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
        for(int j = 1; j < 32; j++) {

            integerArrayList = new ArrayList<>();

            ref.child(String.valueOf(j)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Integer> arrayMedia = new ArrayList<>();

                    double m;
                    int c = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (int i = 0; i <= snapshot.getChildrenCount(); i++) {
                            c++;
                            //Toast.makeText(getApplicationContext(), c+"", Toast.LENGTH_SHORT).show();
                            int data = Integer.parseInt(String.valueOf(snapshot.getValue()));
                            arrayMedia.add(data);
                            if(c == 3){
                                int resultado = Collections.max(arrayMedia);
                                if(resultado == arrayMedia.get(0) && resultado == arrayMedia.get(1) && resultado == arrayMedia.get(2)) {
                                    m = 2;
                                } else if (resultado == arrayMedia.get(1)) {
                                    m = 2;
                                } else if (resultado == arrayMedia.get(0)){
                                    m = 3;
                                } else {
                                    m = 1;
                                }
                                arrayMedia.clear();
                                Toast.makeText(getApplicationContext(), m+"", Toast.LENGTH_SHORT).show();
                                integerArrayList.add((int) m);
                            }

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
          //aqui
        }
        }, 1000);


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