package com.example.moodyduck;

public class Objetivos {
    boolean checked;
    String nome;
    int sequencia;

    public Objetivos(String nome, int sequencia) {
        this.nome = nome;
        this.sequencia = sequencia;
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

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }
}
