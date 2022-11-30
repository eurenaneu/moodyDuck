package com.example.moodyduck;

public class Objetivos {
    boolean checked;
    String nome;

    public Objetivos(String nome) {
        this.nome = nome;
    }

    public Objetivos(String nome, boolean checked) {
        this.checked = checked;
        this.nome = nome;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
