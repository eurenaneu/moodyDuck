package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Config extends AppCompatActivity {
    TextView txtInfo, txtTempo;
    AlertDialog alerta;
    SwitchCompat switchLembretes;
    CardView bHorario, bLembretes;
    Button bRelatorioM, bAlterarSenha, bDeslogar, bObjetivos;
    MaterialTimePicker picker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Calendar c = Calendar.getInstance();
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_config);
        txtInfo = findViewById(R.id.txtInfo);
        bRelatorioM = findViewById(R.id.bRelatorioM);
        bAlterarSenha = findViewById(R.id.bAlterarSenha);
        bDeslogar = findViewById(R.id.bDeslogar);
        bHorario = findViewById(R.id.bHorario);
        bLembretes = findViewById(R.id.bLembretes);
        bObjetivos = findViewById(R.id.bObjetivos);
        switchLembretes = findViewById(R.id.switchLembretes);
        txtTempo = findViewById(R.id.txtTemporizador);
        createNotificationChannel();
        defaultAlarm();

        // setar info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail() == null){
            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lembrarSenha", "false");
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Entrada.class));
            finish();
        } else {
            txtInfo.setText(user.getDisplayName() + "\n" + user.getEmail());
        }

        // onclicks
        bRelatorioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Stats.class));
            }
        });

        bObjetivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaObjetivos.class));
            }
        });

        bAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AlterarSenha.class));
            }
        });

        bDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarConta();
            }
        });

        bLembretes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter == 0) {
                    mostrarTimePicker();
                    switchLembretes.setChecked(true);

                    counter++;
                } else if(counter == 1){
                    cancelAlarm();
                    switchLembretes.setChecked(false);

                    counter--;
                }
            }
        });
    }

    public void deslogarConta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_signout, null);
        Button deslogar = view.findViewById(R.id.bDescartar);
        Button naoDeslogar = view.findViewById(R.id.bNaoDescartar);
        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lembrarSenha", "false");
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Entrada.class));
                finish();
            }
        });
        naoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Home.class));
        this.finish();
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);

        Toast.makeText(getApplicationContext(), "Lembrete desativado", Toast.LENGTH_SHORT).show();
    }

    private void defaultAlarm() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Registros").child(userId).child("alarme").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString() != null) {
                    String alarme = snapshot.getValue().toString();
                    String[] split = alarme.split(":");
                    txtTempo.setText(alarme);

                    c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
                    c.set(Calendar.MINUTE, Integer.parseInt(split[1]));
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                } else {
                    c.set(Calendar.HOUR_OF_DAY, 19);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setAlarm();
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void mostrarTimePicker() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        picker = new MaterialTimePicker.Builder()
                .setTheme(R.style.ThemeOverlay_App_MaterialTimePicker)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(19)
                .setMinute(0)
                .setTitleText("Selecione o horário do alarme")
                .build();

        picker.show(getSupportFragmentManager(), "lembrete");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtTempo.setText(String.format("%02d:%02d", picker.getHour(),picker.getMinute()));

                c.set(Calendar.HOUR_OF_DAY, picker.getHour());
                c.set(Calendar.MINUTE, picker.getMinute());
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                setAlarm();

                ref.child("Users").child(userId).child("alarme").setValue(picker.getHour()+":"+picker.getMinute());
                Toast.makeText(getApplicationContext(), "Lembrete ativado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNotificationChannel(){
        CharSequence name = "lembreteCanal";
        String description = "Canal de lembretes";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("lembrete", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}