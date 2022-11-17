package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class AddRegistro extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    SimpleDateFormat f24 = new SimpleDateFormat("HH:mm");
    DatePickerDialog.OnDateSetListener setListener;
    ImageButton bFeliz, bNeutro, bTriste;
    Calendar c = Calendar.getInstance();
    static String tData;
    Date dataHoraAtual = new Date();
    int dia, mes, ano, hora, min;
    String data, horario;
    TextView tvData;
    Button bVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_registro);
        tvData = findViewById(R.id.tvData);
        bVoltar = findViewById(R.id.bVoltar);
        bFeliz = findViewById(R.id.imageFeliz);
        bNeutro = findViewById(R.id.imageNeutro);
        bTriste = findViewById(R.id.imageTriste);
        preEscolha();
        c.set(dia, mes, ano);
        horario = f24.format(dataHoraAtual);
        data = tData+", "+dia+" de "+nomeMes[mes]+", "+ horario;
        tvData.setText(data);
        setarData();
        onClicks();
    }

    public void onClicks(){
        bVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bFeliz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comecarRegistro("feliz");
            }
        });

        bNeutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comecarRegistro("neutro");
            }
        });

        bTriste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comecarRegistro("triste");
            }
        });
    }

    public void preEscolha(){
        //CONTINUAR AQUI
        int diminuirMes = 0;

        if(tData.equals("ontem")){
            int ontem = c.get(Calendar.DAY_OF_MONTH) - 1;

            if(ontem < 1 && (c.get(Calendar.MONTH)+1)%2 == 0){
                dia = 31 + ontem;
                diminuirMes++;
            } else if(ontem < 1 && (c.get(Calendar.MONTH)+1)%2 != 0){
                dia = 30 + ontem;
                diminuirMes++;
            } else {
                dia = c.get(Calendar.DAY_OF_MONTH) - 1;
            }
        } else if(tData.equals("outro dia")){

            int outroDia = c.get(Calendar.DAY_OF_MONTH) - 2;

            if(outroDia < 1 && (c.get(Calendar.MONTH)+1)%2 == 0){
                dia = 31 + outroDia;
                diminuirMes++;
            } else if(outroDia < 1 && (c.get(Calendar.MONTH)+1)%2 != 0){
                dia = 30 + outroDia;
                diminuirMes++;
            } else {
                dia = c.get(Calendar.DAY_OF_MONTH) - 2;
            }

        } else {
            dia = c.get(Calendar.DAY_OF_MONTH);
        }

        mes = c.get(Calendar.MONTH) - diminuirMes;
        ano = c.get(Calendar.YEAR);
    }

    public void setarData(){
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRegistro.this, android.R.style.Theme_Holo_Dialog_MinWidth,setListener,ano,mes,dia);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                String md = "01/01/2000 00:00:01";
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(md);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long minDate = date.getTime();
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month == mes && year == ano){
                    //if(dayOfMonth == dia)
                    //    tData = "Hoje";
                    //else if(dayOfMonth == dia-1){
                    //    tData = "Ontem";
                    //}

                    //else if(dayOfMonth == dia-2){
                    //    tData = "Anteontem";
                    //}
                    //else{
                    //    tData = "Outro dia";
                    //}
                } else {
                    tData = "outro dia";
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

    public void comecarRegistro(String humor){
        FormRegistro.humor = humor;
        FormRegistro.horario = f24.format(dataHoraAtual);
        FormRegistro.dia = dia;
        FormRegistro.mes = mes;
        FormRegistro.ano = ano;
        startActivity(new Intent(this, FormRegistro.class));
    }
}