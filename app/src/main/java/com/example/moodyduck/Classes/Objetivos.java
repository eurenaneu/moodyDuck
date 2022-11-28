package com.example.moodyduck.Classes;

public class Objetivos {
    String nome;
    int sequencia;

    public Objetivos(String nome, int sequencia) {
        this.nome = nome;
        this.sequencia = sequencia;
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
