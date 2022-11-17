package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlterarSenha extends AppCompatActivity {
    EditText senhaAntiga, senhaNova, confirmaSenhaNova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        senhaAntiga = findViewById(R.id.campoSenhaAntiga);
        senhaNova = findViewById(R.id.campoSenhaNova);
        confirmaSenhaNova = findViewById(R.id.campoConfirmaNovaSenha);
    }

    public void alterarSenha(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String oldpswd = senhaAntiga.getText().toString();
        String newpswd = senhaNova.getText().toString();
        String confirmnewpswd = confirmaSenhaNova.getText().toString();
        if (!newpswd.equals(confirmnewpswd)) {
            Snackbar snackbar = Snackbar.make(view, "Senhas novas não coincidem", 2000);
            snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        } else if (newpswd.trim().isEmpty() || confirmnewpswd.trim().isEmpty()){
            Snackbar snackbar = Snackbar.make(view, "Campos de senha vazios", 2000);
            snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldpswd);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        user.updatePassword(newpswd).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Snackbar snackbar = Snackbar.make(view, "Senha atualizada", 2000);
                                    snackbar.setBackgroundTint(Color.rgb(48, 207, 122));
                                    snackbar.setTextColor(Color.WHITE);
                                    snackbar.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getApplicationContext(), Home.class));
                                        }
                                    }, 2000);
                                } else {
                                    Snackbar snackbar = Snackbar.make(view, "Algo de errado ocorreu, tente novamente", 2000);
                                    snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
                                    snackbar.setTextColor(Color.WHITE);
                                    snackbar.show();
                                }
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(view, "Não foi possível autenticar (senha incorreta)", 2000);
                        snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            });
        }
    }
}