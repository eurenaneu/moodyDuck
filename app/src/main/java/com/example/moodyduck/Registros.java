package com.example.moodyduck;

public class Registros {
    String humor, horario, data;
    long timestamp;

    public Registros(String humor, String horario, String data, long timestamp) { // constructor para registro
        this.humor = humor;
        this.horario = horario;
        this.data = data;
        this.timestamp = timestamp;
    }

    public Registros(String humor, String data){ // constructor para recyclerview
        this.humor = humor;
        this.data = data;
    }

    public Registros() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHumor() {
        return humor;
    }

    public void setHumor(String humor) {
        this.humor = humor;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
