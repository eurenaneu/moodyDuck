package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FormRegistro extends AppCompatActivity {
    String[] nomeMes = {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"};
    Calendar c = Calendar.getInstance();
    static int dia, mes, ano;
    AlertDialog alerta;
    Adaptador adaptador;
    ImageButton bSalvar;
    ArrayList<Objetivos> objetivos = new ArrayList<>();
    static String humor, horario;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);
        bSalvar = findViewById(R.id.btnSalvar);
            bSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salvarRegistros();
                }
            });
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        setupRecycler();
    }

    public void setupSelectItems(){ // ISSO AQUI MOSTRA OS OBJETIVOS SELECIONADOS
        try{
            for(int i = 0; i < adaptador.getSelected().size(); i++){
                String nomeObjetivo = adaptador.getSelected().get(i).getNome(); // nome do objetivo
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupRecycler() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(user.getUid()).child("Objetivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try {
                        if (Boolean.parseBoolean(dataSnapshot.child("ativo").getValue().toString())) {
                            String nome = dataSnapshot.child("nome").getValue().toString();
                            int sequencia = Integer.parseInt(dataSnapshot.child("sequencia").getValue().toString());

                            Objetivos o = new Objetivos(nome, sequencia);
                            objetivos.add(o);
                            adaptador = new Adaptador(getApplicationContext(), objetivos, 2);
                            rv.setAdapter(adaptador);
                            rv.setLayoutManager(new LinearLayoutManager(FormRegistro.this));
                            adaptador.notify();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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

    public void salvarRegistros (){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String childName = dia + "-" + nomeMes[mes] + "-" + ano + " - " + horario + ":" + c.get(Calendar.SECOND);
        String data = dia + "/" + (mes + 1) + "/" + ano;
        String[] myDate = data.split("/");
        Date date = new Date(Integer.parseInt(myDate[2]), Integer.parseInt(myDate[1]), Integer.parseInt(myDate[0]));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Registros r = new Registros(humor, horario, data, date.getTime());
        ref.child("Users").child(user.getUid()).child("Registros").child(String.valueOf(ano)).child(nomeMes[mes]).child(childName).setValue(r);
        startActivity(new Intent(this, Home.class));
        setupSelectItems();
    }
}