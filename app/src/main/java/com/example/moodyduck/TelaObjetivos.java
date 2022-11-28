package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaObjetivos extends AppCompatActivity {
    Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_objetivos);
        voltar = findViewById(R.id.bVoltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });
    }

    private void voltar(){
        startActivity(new Intent(this, Home.class));
        this.finish();
    }
}