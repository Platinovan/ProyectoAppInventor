package com.tarea.proyectoappinventor;

import android.widget.Toast;

public class Usuario {
    //Se deben de poner los key que estan en la base de datos
    String Apodo, Imagen, Correo, SeUnio, Uid;
    int Puntuacion;

    public Usuario(){
    }

    public Usuario(String apodo, String correo, String seUnio, String uid, String imagen, int puntaje) {
        Apodo = apodo;
        Imagen = imagen;
        Puntuacion = puntaje;
        Correo = correo;
        SeUnio = seUnio;
        Uid = uid;
    }

    public String getApodo() {
        return Apodo;
    }

    public void setApodo(String apodo) {
        Apodo = apodo;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public int getPuntuacion() {
        return Puntuacion;
    }

    public void setPuntuacion(int puntaje) {
        Puntuacion = puntaje;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getSeUnio() {
        return SeUnio;
    }

    public void setSeUnio(String seUnio) {
        SeUnio = seUnio;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
