package com.example.moodyduck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Config extends AppCompatActivity {
    TextView txtInfo;
    AlertDialog alerta;
    SwitchCompat switchLembretes;
    Button bRelatorioM, bAlterarSenha, bDeslogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MoodyDuckSecondary);
        setContentView(R.layout.activity_config);
        txtInfo = findViewById(R.id.txtInfo);
        bRelatorioM = findViewById(R.id.bRelatorioM);
        bAlterarSenha = findViewById(R.id.bAlterarSenha);
        bDeslogar = findViewById(R.id.bDeslogar);
        switchLembretes = findViewById(R.id.switchLembretes);

        // setar info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        txtInfo.setText(user.getDisplayName()+"\n"+user.getEmail());

        // onclicks
        bRelatorioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Stats.class));
            }
        });

        bAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AlterarSenha.class));
            }
        });

        bDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarConta();
            }
        });
    }

    public void deslogarConta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_signout, null);
        Button deslogar = view.findViewById(R.id.bDescartar);
        Button naoDeslogar = view.findViewById(R.id.bNaoDescartar);
        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lembrarSenha", "false");
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Entrada.class));
                finish();
            }
        });
        naoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Home.class));
        this.finish();
    }

    public void ativarLembretes(View view){
        switchLembretes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Snackbar snackbar = Snackbar.make(view, "Lembretes ativados", 2000);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Lembretes desativados", 2000);
                    snackbar.show();
                }
            }
        });
    }
}