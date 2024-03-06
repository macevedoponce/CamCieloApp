package com.acevedo.caminoalcielo.Clases;

import androidx.annotation.NonNull;

public class Alumnos {
    int user_id;
    String user_nombres;
    String user_apellidos;
    String user_dni;
    String user_foto;

    public Alumnos(int user_id, String user_nombres, String user_apellidos, String user_dni, String user_foto) {
        this.user_id = user_id;
        this.user_nombres = user_nombres;
        this.user_apellidos = user_apellidos;
        this.user_dni = user_dni;
        this.user_foto = user_foto;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_nombres() {
        return user_nombres;
    }

    public void setUser_nombres(String user_nombres) {
        this.user_nombres = user_nombres;
    }

    public String getUser_apellidos() {
        return user_apellidos;
    }

    public void setUser_apellidos(String user_apellidos) {
        this.user_apellidos = user_apellidos;
    }

    public String getUser_dni() {
        return user_dni;
    }

    public void setUser_dni(String user_dni) {
        this.user_dni = user_dni;
    }

    public String getUser_foto() {
        return user_foto;
    }

    public void setUser_foto(String user_foto) {
        this.user_foto = user_foto;
    }

    @NonNull
    @Override
    public String toString() {
        return "Alumnos{" +
                "user_id=" + user_id +
                ", user_nombres='" + user_nombres + '\'' +
                ", user_apellidos='" + user_apellidos + '\'' +
                ", user_dni='" + user_dni + '\'' +
                ", user_foto='" + user_foto + '\'' +
                '}';
    }
}
