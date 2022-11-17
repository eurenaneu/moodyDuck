package com.example.moodyduck;

public class Registros {
    String humor, horario, data;
    int img;

    public Registros(String humor, String horario, String data) {
        this.humor = humor;
        this.horario = horario;
        this.data = data;
    }

    public Registros(String humor, String data){
        this.humor = humor;
        this.data = data;
    }

    public Registros(String humor, String data, int img){
        this.humor = humor;
        this.data = data;
        this.img = img;
    }

    public Registros() {
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
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
