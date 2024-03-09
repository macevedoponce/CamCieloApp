package com.acevedo.caminoalcielo.Clases;

import androidx.annotation.NonNull;

public class Alumnos {
    int user_id;
    String user_nombres;
    String user_apellidos;
    String user_dni;
    String user_foto;
    String puntos_participacion;
    String puntos_asistencia;
    String puntos_biblia;

    String total_puntos;

    public Alumnos(String user_nombres, String user_apellidos,  String user_foto, String total_puntos) {
        this.user_nombres = user_nombres;
        this.user_apellidos = user_apellidos;
        this.user_foto = user_foto;
        this.total_puntos = total_puntos;
    }

    public Alumnos(int user_id, String user_nombres, String user_apellidos, String user_dni, String user_foto, String puntos_participacion, String puntos_asistencia, String puntos_biblia) {
        this.user_id = user_id;
        this.user_nombres = user_nombres;
        this.user_apellidos = user_apellidos;
        this.user_dni = user_dni;
        this.user_foto = user_foto;
        this.puntos_participacion = puntos_participacion;
        this.puntos_asistencia = puntos_asistencia;
        this.puntos_biblia = puntos_biblia;
    }

    public String getPuntos_participacion() {
        return puntos_participacion;
    }

    public void setPuntos_participacion(String puntos_participacion) {
        this.puntos_participacion = puntos_participacion;
    }

    public String getPuntos_asistencia() {
        return puntos_asistencia;
    }

    public void setPuntos_asistencia(String puntos_asistencia) {
        this.puntos_asistencia = puntos_asistencia;
    }

    public String getPuntos_biblia() {
        return puntos_biblia;
    }

    public void setPuntos_biblia(String puntos_biblia) {
        this.puntos_biblia = puntos_biblia;
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

    public String getTotal_puntos() {
        return total_puntos;
    }

    public void setTotal_puntos(String total_puntos) {
        this.total_puntos = total_puntos;
    }
}
