package com.example.moodyduck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class FormRegistro extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    Calendar c = Calendar.getInstance();
    static int dia, mes, ano;
    AlertDialog alerta;
    static String humor, horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_returnhome, null);
        Button descartar = view.findViewById(R.id.bDescartar);
        Button naoDescartar = view.findViewById(R.id.bNaoDescartar);
        descartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });
        naoDescartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    public void salvarRegistro(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String childName = dia+"-"+nomeMes[mes]+"-"+ano+" - "+horario+":"+c.get(Calendar.SECOND);
        String data = dia+"/"+(mes+1)+"/"+ano;
        String[] myDate = data.split("/");
        Date date = new Date(Integer.parseInt(myDate[2]), Integer.parseInt(myDate[1]), Integer.parseInt(myDate[0]));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(childName);
        Registros r = new Registros(humor, horario, dia+"/"+(mes+1)+"/"+ano, date.getTime());
        ref.setValue(r);
        startActivity(new Intent(this, Home.class));
    }
}