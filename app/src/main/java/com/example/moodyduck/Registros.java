package com.example.moodyduck;

public class Registros {
    String nome, horario, data;

    public Registros(String nome, String horario, String data) {
        this.nome = nome;
        this.horario = horario;
        this.data = data;
    }

    public Registros(String nome, String data){
        this.nome = nome;
        this.data = data;
    }

    public Registros() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
