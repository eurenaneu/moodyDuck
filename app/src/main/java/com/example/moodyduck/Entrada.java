package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Entrada extends AppCompatActivity {
    TextView appName, bemvindo, divider;
    Button cadastro, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        appName = findViewById(R.id.txtAppname);
        bemvindo = findViewById(R.id.txtBemvindo);
        cadastro = findViewById(R.id.bCadastro);
        login = findViewById(R.id.bLogin);
        divider = findViewById(R.id.divider);
        String text = "<font color=#ffffff>Moody</font><font color=#fffab9>Duck</font><font color=#ffffff>!</font>";
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