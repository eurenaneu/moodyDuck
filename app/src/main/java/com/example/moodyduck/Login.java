package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText email, senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.campoEmail);
        senha = findViewById(R.id.campoSenha);
    }

    public void mostrar(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logarTeste(View v) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Usuario");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean logado = false;
                String e = email.getText().toString();
                String s = senha.getText().toString();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Usuario u = d.getValue(Usuario.class);
                    if (u.getEmail().equals(e) && u.getSenha().equals(s)) {
                        logado = true;
                        mudarTela();
                        break;
                    }
                }

                if(logado == false){
                mostrar("Desculpe, não encontramos essa conta");
            }
        }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void mudarTela(){
        startActivity(new Intent(this, Home.class));
    }
}