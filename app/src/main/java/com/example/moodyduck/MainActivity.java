package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView alexandre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alexandre = findViewById(R.id.txtAppname);
        String text = "<font color=#ffffff>Moody</font><font color=#fffab9>Duck</font><font color=#ffffff>!</font>";
        alexandre.setText(Html.fromHtml(text));
    }

    public void irCadastro(View v){
        startActivity(new Intent(this, Cadastro.class));
    }

    public void irLogin(View v){
        startActivity(new Intent(this, Login.class));
    }
}