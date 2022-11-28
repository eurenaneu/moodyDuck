package com.example.moodyduck.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario{
    String user, senha, email;

    public Usuario(String user, String email, String senha) {
        this.user = user;
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public void cadastrarUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(email).setValue(this);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}