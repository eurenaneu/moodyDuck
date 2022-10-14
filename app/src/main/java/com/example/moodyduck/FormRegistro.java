package com.example.moodyduck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FormRegistro extends AppCompatActivity {
    AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        TextView txt_pop = findViewById(R.id.txtPopup);
        Button deslogar = view.findViewById(R.id.bDeslogar);
        Button naoDeslogar = view.findViewById(R.id.bFake);
        txt_pop.setText("Deseja retornar à tela inicial? Todo o seu progresso será descartado.");
        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int ano = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(String.valueOf(dia)).child(humor);
                startActivity(new Intent(getApplicationContext(), Home.class));
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
}