package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class Stats extends AppCompatActivity {
    LineChart lc;
    ArrayList lineArrayList = new ArrayList<>();
    int bd;
    DatabaseReference ref;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_stats);
        lineArrayList = new ArrayList<>();
        LineDataSet lineDataset = new LineDataSet(lineArrayList, "");
        lineDataset.setValueTextColor(ContextCompat.getColor(this, R.color.amalero));
        lineDataset.setValueTextSize(16);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "qckbold.ttf");
        lineDataset.setValueTypeface(tf);
        lineDataset.setDrawCircles(false);
        lineDataset.setColor(ContextCompat.getColor(this, R.color.amalero));
        LineData lineData = new LineData(lineDataset);
        lc.setNoDataText("Nenhum registro até agora :)");
        lc.setNoDataTextColor(Color.rgb(255,250,185));
        lc.setData(lineData);
        lc.getDescription().setEnabled(false);
        lc.setTouchEnabled(false);
        lc.setPinchZoom(false);
        lc.getAxisRight().setEnabled(false);
        lc.getAxisLeft().setEnabled(false);
        lc.getXAxis().setEnabled(false);
        lc.getAxisRight().setDrawGridLines(false);
        lc.getAxisLeft().setDrawGridLines(false);
        lc.getXAxis().setDrawGridLines(false);
        lc.setDoubleTapToZoomEnabled(false);
        lc.setBorderColor(Color.WHITE);
        lc.getLegend().setEnabled(false);
        inicializar();
    }

    public void inicializar(){
        lc = findViewById(R.id.linechart);
    }

    /*public void montaGrafico(View view){
        barChart.clear();
        for(int i = 0; i < integerArrayList.size(); i++) {
            barArrayList.add(new BarEntry(i, integerArrayList.get(i)));
        }
        BarDataSet barDataset = new BarDataSet(barArrayList, "");
        barDataset.setColors(Color.WHITE);
        barDataset.setValueTextColor(Color.BLACK);
        barDataset.setValueTextSize(16f);
        BarData barData = new BarData(barDataset);
        barChart.setData(barData);

    }
    public void visualizarDados(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(int i = 0; i <= snapshot.getChildrenCount(); i++) {
                        test = Integer.parseInt(snapshot.getValue().toString());
                        integerArrayList.add(test);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}