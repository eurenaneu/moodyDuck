package com.example.moodyduck;

public class Objetivos {
    boolean checked, ativo;
    String nome;

    public Objetivos(String nome) {
        this.nome = nome;
    }

    public Objetivos(String nome, boolean ativo) {
        this.nome = nome;
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
