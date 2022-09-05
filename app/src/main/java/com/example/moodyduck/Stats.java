package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
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
    ArrayList lineArrayList;
    int bd = 0;
    DatabaseReference ref;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_stats);
        lc = findViewById(R.id.linechart);
        lineArrayList = new ArrayList();
        getData();
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
    }

    public void getData(){
        String[] nomeMes = new String[]{"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
        ref = FirebaseDatabase.getInstance().getReference().child(nomeMes[c.get(Calendar.MONTH)]+"-"+c.get(Calendar.YEAR));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    bd = (int) dataSnapshot.getValue();
                    lineArrayList.add(new BarEntry(1f, bd));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lineArrayList.add(new BarEntry(1f, bd));
        lineArrayList.add(new BarEntry(2f, 2));
        lineArrayList.add(new BarEntry(3f, 4));
        lineArrayList.add(new BarEntry(4f, 8));
        lineArrayList.add(new BarEntry(5f, 16));
        lineArrayList.add(new BarEntry(6f, 32));
        lineArrayList.add(new BarEntry(7f, 64));
        lineArrayList.add(new BarEntry(8f, 128));
        lineArrayList.add(new BarEntry(9f, 256));
        lineArrayList.add(new BarEntry(10f, 512));
        lineArrayList.add(new BarEntry(11f, 1024));
        lineArrayList.add(new BarEntry(12f, 2048));
        lineArrayList.add(new BarEntry(13f, 4096));
    }
}