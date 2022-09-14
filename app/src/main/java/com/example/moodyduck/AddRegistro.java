package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class AddRegistro extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener setListener;
    Calendar c = Calendar.getInstance();
    int dia, mes, ano;
    String data;
    TextView tvData;
    Button bVoltar;
    Date dataHoraAtual = new Date();
    static String tData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_registro);
        tvData = findViewById(R.id.tvData);
        bVoltar = findViewById(R.id.bVoltar);
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);
        c.set(dia, mes, ano);
        String hora = new SimpleDateFormat("HH:mm").format(dataHoraAtual);
        String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};

        //pre-escolha
        if(tData.equals("Ontem")){
            data = tData+", "+(dia-1)+" de "+nomeMes[mes]+", "+ hora;
            tvData.setText(data);
        }

        else if(tData.equals("Hoje")){
            data = tData+", "+dia+" de "+nomeMes[mes]+", "+ hora;
            tvData.setText(data);
        }

        else if(tData.equals("Outro dia")){
            data = tData+", "+(dia+1)+" de "+nomeMes[mes]+", "+ hora;
            tvData.setText(data);
        }

        bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRegistro.this, AddRegistro.class));
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRegistro.this, android.R.style.Theme_Holo_Dialog_MinWidth,setListener,ano,mes,dia);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month == mes && year == ano){
                    if(dayOfMonth == dia)
                        tData = "Hoje";
                    else if(dayOfMonth == dia-1){
                        tData = "Ontem";
                    }
                    else if(dayOfMonth == dia-2){
                        tData = "Anteontem";
                    }
                    else{
                        tData = "Outro dia";
                    }
                }
                String data = tData+", "+dayOfMonth+" de "+nomeMes[month]+", ";
                tvData.setText(data);
            }
        };
    }
}