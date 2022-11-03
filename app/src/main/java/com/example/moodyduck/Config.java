package com.example.moodyduck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Config extends AppCompatActivity {
    TextView txtInfo;
    Button bRelatorioM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_config);
        txtInfo = findViewById(R.id.txtInfo);
        bRelatorioM = findViewById(R.id.button5);

        // setar info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        txtInfo.setText(user.getDisplayName()+"\n"+user.getEmail());

        // onclicks
        bRelatorioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Config.this, Stats.class));
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(this, Home.class));
        finish();
    }
}