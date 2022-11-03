package com.example.moodyduck;

public class Objetivos {
    String nome;
    int progresso;

    public Objetivos(String nome, int progresso) {
        this.nome = nome;
        this.progresso = progresso;
    }

    public Objetivos() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }
}
