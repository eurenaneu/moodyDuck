package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastro extends AppCompatActivity {
    TextView aviso;
    EditText email, senha, confirma;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        aviso = findViewById(R.id.avisoSenhas);
        email = findViewById(R.id.campoEmail);
        senha = findViewById(R.id.campoSenha);
        confirma = findViewById(R.id.campoConfirma);
        fab = findViewById(R.id.fab);
        aviso.setVisibility(View.GONE);
    }

    public void irHome(View v){
        startActivity(new Intent(this, Home.class));
    }

    public void cadastrarUser(View v){
        String e = email.getText().toString();
        String s = senha.getText().toString();
        String cs = confirma.getText().toString();
        if(s != cs) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(e, s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //continuar depois
                    }
                }
            });
        }
    }
}