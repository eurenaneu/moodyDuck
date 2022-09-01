package com.example.moodyduck;

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

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Stats extends AppCompatActivity {
    LineChart lc;
    ArrayList lineArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        lc = findViewById(R.id.linechart);
        lineArrayList = new ArrayList();
        lineArrayList.add(new BarEntry(2f, 10));
        lineArrayList.add(new BarEntry(3f, 20));
        lineArrayList.add(new BarEntry(4f, 30));
        lineArrayList.add(new BarEntry(5f, 40));
        lineArrayList.add(new BarEntry(2f, 10));
        lineArrayList.add(new BarEntry(3f, 20));
        lineArrayList.add(new BarEntry(4f, 30));
        lineArrayList.add(new BarEntry(5f, 40));
        lineArrayList.add(new BarEntry(2f, 10));
        lineArrayList.add(new BarEntry(3f, 20));
        lineArrayList.add(new BarEntry(4f, 30));
        lineArrayList.add(new BarEntry(5f, 40));
        LineDataSet lineDataset = new LineDataSet(lineArrayList, "");
        lineDataset.setValueTextColor(R.color.amalero);
        lineDataset.setValueTextSize(16);
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
}