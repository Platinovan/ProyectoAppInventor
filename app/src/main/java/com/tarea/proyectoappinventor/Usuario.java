package com.tarea.proyectoappinventor;

public class Usuario {
    String Apodo, Password, Correo, SeUnio, Uid, Imagen;
    int Puntaje;

    public Usuario(){
    }

    public Usuario(String apodo, String password, String correo, String seUnio, String uid, String imagen, int puntaje) {
        Apodo = apodo;
        Password = password;
        Correo = correo;
        SeUnio = seUnio;
        Uid = uid;
        Imagen = imagen;
        Puntaje = puntaje;
    }

    public String getApodo() {
        return Apodo;
    }

    public void setApodo(String apodo) {
        Apodo = apodo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }
}
