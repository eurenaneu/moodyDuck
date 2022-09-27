package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Cadastro extends AppCompatActivity {
    TextView aviso;
    EditText senha, confirma;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        aviso = findViewById(R.id.avisoSenhas);
        senha = findViewById(R.id.campoSenha);
        confirma = findViewById(R.id.campoConfirma);
        fab = findViewById(R.id.fab);
        aviso.setVisibility(View.GONE);
    }

    public void irHome(View v){
        startActivity(new Intent(this, Home.class));
    }
}

// aoaooaoaoa vida de gado