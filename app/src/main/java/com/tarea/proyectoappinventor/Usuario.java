package com.tarea.proyectoappinventor;

public class Usuario {
    String Apodo, MejorPuntuacion, JuegaDesde, password;
    int Puntaje;

    public Usuario(){

    }

    public Usuario(String apodo, String mejorPuntuacion, String juegaDesde, String password, int puntaje) {
        Apodo = apodo;
        MejorPuntuacion = mejorPuntuacion;
        JuegaDesde = juegaDesde;
        this.password = password;
        Puntaje = puntaje;
    }

    public String getApodo() {
        return Apodo;
    }

    public void setApodo(String apodo) {
        Apodo = apodo;
    }

    public String getMejorPuntuacion() {
        return MejorPuntuacion;
    }

    public void setMejorPuntuacion(String mejorPuntuacion) {
        MejorPuntuacion = mejorPuntuacion;
    }

    public String getJuegaDesde() {
        return JuegaDesde;
    }

    public void setJuegaDesde(String juegaDesde) {
        JuegaDesde = juegaDesde;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }
}
