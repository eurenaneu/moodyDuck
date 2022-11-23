package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Entrada extends AppCompatActivity {
    TextView appName, bemvindo, divider;
    ImageView imgMoody;
    Button cadastro, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        appName = findViewById(R.id.txtAppname);
        bemvindo = findViewById(R.id.txtBemvindo);
        imgMoody = findViewById(R.id.imgMoody);
        cadastro = findViewById(R.id.bCadastro);
        login = findViewById(R.id.bLogin);
        divider = findViewById(R.id.divider);
        String text = "<font color=#ffffff>Moody</font><font color=#fffab9>Duck</font><font color=#ffffff>!</font>";
        appName.setText(Html.fromHtml(text));
        imgMoody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quack();
            }
        });
    }

    public void quack(){
        divider.setText("quack?");
        cadastro.setText("Quack");
        login.setText("Quack");
        bemvindo.setText("Quack quack quack quack!");
        String text = "<font color=#ffffff>Quack</font><font color=#fffab9>Quack</font><font color=#ffffff>!</font>";
        appName.setText(Html.fromHtml(text));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.moveTaskToBack(true);
    }



    public void irCadastro(View v){
        startActivity(new Intent(this, Cadastro.class));
    }

    public void irLogin(View v){
        startActivity(new Intent(this, Login.class));
    }
}