package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddRegistro extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    SimpleDateFormat f24 = new SimpleDateFormat("HH:mm");
    DatePickerDialog.OnDateSetListener setListener;
    Calendar c = Calendar.getInstance();
    Date dataHoraAtual = new Date();
    int dia, mes, ano, hora, min;
    String data, horario;
    static String tData;
    TextView tvData;
    Button bVoltar;

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
        horario = f24.format(dataHoraAtual);
        preEscolha();
        setarData();
        bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void preEscolha(){
        //pre-escolha
        if(tData.equals("Ontem")){
            data = tData+", "+(dia-1)+" de "+nomeMes[mes]+", "+ horario;
            tvData.setText(data);
        }

        else if(tData.equals("Hoje")){
            data = tData+", "+dia+" de "+nomeMes[mes]+", "+ horario;
            tvData.setText(data);
        }

        else if(tData.equals("Outro dia")){
            data = tData+", "+(dia-3)+" de "+nomeMes[mes]+", "+ horario;
            tvData.setText(data);
        }
    }

    public void setarData(){
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRegistro.this, android.R.style.Theme_Holo_Dialog_MinWidth,setListener,ano,mes,dia);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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
                } else {
                    tData = "Outro dia";
                }

                if(year != ano){
                    data = tData+", "+dayOfMonth+" de "+nomeMes[month]+" de "+year;
                } else {
                    data = tData + ", " + dayOfMonth + " de " + nomeMes[month];
                }
                dia = dayOfMonth;
                ano = year;
                mes = month;
                tvData.setText(data);
                setarHorario();
            }
        };
    }

    public void setarHorario(){
            TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hora = hourOfDay;
                    min = minute;
                    horario = hourOfDay+":"+minute;
                    try {
                        dataHoraAtual = f24.parse(horario);
                        tvData.setText(data+=", "+f24.format(dataHoraAtual));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddRegistro.this, android.R.style.Theme_Holo_Dialog_MinWidth, timePickerListener, 12, 0, true);
            timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    tvData.setText(data+=", "+horario);
                }
            });
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(hora, min);
            timePickerDialog.show();
    }

    public void comecarRegistro(View v){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        ref.child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(String.valueOf(dia)).setValue(3);
        startActivity(new Intent(this, FormRegistro.class));
    }
}