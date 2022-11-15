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
        View view = getLayoutInflater().inflate(R.layout.custom_dialog2, null);
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
        DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(dia+"-"+nomeMes[mes]+"-"+ano+" - "+horario+":"+c.get(Calendar.SECOND));
        Registros r = new Registros(humor, horario, dia+"."+(mes+1)+"."+ano);
        /*path.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (humor) {
                    case "feliz":
                        if (snapshot.child("feliz").exists()) {
                            int valor = Integer.parseInt(snapshot.child("feliz").getValue().toString()) + 1;
                            path.child("feliz").setValue(valor);
                        } else {
                            path.child("feliz").setValue(1);
                        }
                        break;
                    case "neutro":
                        if (snapshot.child("neutro").exists()) {
                            int valor = Integer.parseInt(snapshot.child("neutro").getValue().toString()) + 1;
                            path.child("neutro").setValue(valor);
                        } else {
                            path.child("neutro").setValue(1);
                        }
                        break;
                    case "triste":
                        if (snapshot.child("triste").exists()) {
                            int valor = Integer.parseInt(snapshot.child("triste").getValue().toString()) + 1;
                            path.child("triste").setValue(valor);
                        } else {
                            path.child("triste").setValue(1);
                        }
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        path.setValue(r);
        startActivity(new Intent(this, Home.class));
    }
}