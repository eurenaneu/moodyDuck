package com.example.moodyduck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {
    EditText nome, email, senha, confirma;
    FloatingActionButton fab;
    TextView aviso;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        aviso = findViewById(R.id.avisoSenhas);
        nome = findViewById(R.id.campoUser);
        email = findViewById(R.id.campoSenhaAntiga);
        senha = findViewById(R.id.campoSenhaNova);
        confirma = findViewById(R.id.campoConfirma);
        fab = findViewById(R.id.fab);
        aviso.setVisibility(View.GONE);
    }

    public void irHome(){
        startActivity(new Intent(this, Home.class));
    }

    public void cadastrarUser(View v) {
        String e = email.getText().toString();
        String s = senha.getText().toString();
        String c = confirma.getText().toString();
        String u = nome.getText().toString();

        if(u.trim().isEmpty() || e.trim().isEmpty() || s.trim().isEmpty() || c.trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(v, "Favor, preencher todos os campos corretamente", 2000);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
            snackbar.show();
        } else if (!s.equals(c)){
            Snackbar snackbar = Snackbar.make(v, "Senhas não coincidem", 2000);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
            snackbar.show();
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(e, s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        salvarUser(e);
                        Snackbar snackbar = Snackbar.make(v, "Conta criada com sucesso", 2000);
                        snackbar.setBackgroundTint(Color.rgb(48, 207, 122));
                        snackbar.show();
                        irHome();
                    } else {
                        String erro;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Digite uma senha com, no mínimo, 6 caracteres";
                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = "Essa conta já está cadastrada";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "E-mail inválido";
                        } catch (Exception e) {
                            erro = "Erro desconhecido";
                        }
                        Snackbar snackbar = Snackbar.make(v, erro, 2000);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.rgb(255, 87, 84));
                        snackbar.show();
                    }
                }
            });
        }
    }

    public void salvarUser(String e){
        String u = nome.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(u).build();
        user.updateProfile(profileUpdates);
        ArrayList<Objetivos> addDefault = new ArrayList<>();
        Objetivos dormirCedo = new Objetivos("dormirCedo", false);
        Objetivos estudarMais = new Objetivos("estudarMais", false);
        Objetivos comerSaudavel = new Objetivos("comerSaudavel", false);
        Objetivos fazerExercicio = new Objetivos("fazerExercicio", false);
        Objetivos realizarFaxina = new Objetivos("realizarFaxina", false);
        addDefault.add(dormirCedo);
        addDefault.add(estudarMais);
        addDefault.add(comerSaudavel);
        addDefault.add(fazerExercicio);
        addDefault.add(realizarFaxina);
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", u);
        usuarios.put("email", e);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.child("Users").child(userId).setValue(usuarios);
        db.child("Users").child(userId).child("alarmeSwitch").setValue(false);
        for(int i = 0; i < addDefault.size(); i++) {
            db.child("Users").child(userId).child("Objetivos").child(addDefault.get(i).getNome()).setValue(addDefault.get(i));
        }
    }
}