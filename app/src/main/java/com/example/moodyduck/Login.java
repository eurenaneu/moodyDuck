package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText email, senha;
    CheckBox cbp;
    ProgressBar pB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.campoEmail);
        senha = findViewById(R.id.campoSenha);
        pB = findViewById(R.id.progressBar);
        cbp = findViewById(R.id.checkBox);
        lembrarSenha();
    }

    public void lembrarSenha(){
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("lembrarSenha", "");
        if(checkbox.equals("true")){
            startActivity(new Intent(Login.this, Home.class));
        } else if(checkbox.equals("false")){

        }

        cbp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences ("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString ("lembrarSenha", "true");
                    editor.apply();
                }else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("lembrarSenha", "false");
                    editor.apply();
                }
            }
        });
    }

    public void logarUser(View v) {
        String e = email.getText().toString();
        String s = senha.getText().toString();

        if(e.trim().isEmpty() || s.trim().isEmpty()){
            Snackbar snackbar = Snackbar.make(v, "Campos vazios", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        }

        else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(e, s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        pB.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Login.this, Home.class));
                            }
                        }, 3000);
                    } else {
                        String erro;

                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            erro = "Erro desconhecido ao se logar";
                        }
                        Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            });
        }
    }
}