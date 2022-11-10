package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class FormRegistro extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    AlertDialog alerta;
    static String humor;
    int h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        addValor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removerValor();
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
                removerValor();
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

    private void removerValor() {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(String.valueOf(dia)).child(humor);
        path.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                h = Integer.parseInt(snapshot.getValue().toString());
                if(snapshot.getValue() != null) {
                    if (h != 0) {
                        h--;
                        path.setValue(h);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addValor() {
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(String.valueOf(dia)).child(humor);
        path.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                h = Integer.parseInt(snapshot.getValue().toString());
                if(snapshot.getValue() != null) {
                    h++;
                    path.setValue(h);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}