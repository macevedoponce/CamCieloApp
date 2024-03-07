package com.acevedo.caminoalcielo.Clases;

public class Puntos {

        int puntos_participacion;
        int puntos_asistencia;
        int puntos_biblia;
        String puntos_fecha;

    public Puntos(int puntos_participacion, int puntos_asistencia, int puntos_biblia, String puntos_fecha) {
        this.puntos_participacion = puntos_participacion;
        this.puntos_asistencia = puntos_asistencia;
        this.puntos_biblia = puntos_biblia;
        this.puntos_fecha = puntos_fecha;
    }

    public int getPuntos_participacion() {
        return puntos_participacion;
    }

    public void setPuntos_participacion(int puntos_participacion) {
        this.puntos_participacion = puntos_participacion;
    }

    public int getPuntos_asistencia() {
        return puntos_asistencia;
    }

    public void setPuntos_asistencia(int puntos_asistencia) {
        this.puntos_asistencia = puntos_asistencia;
    }

    public int getPuntos_biblia() {
        return puntos_biblia;
    }

    public void setPuntos_biblia(int puntos_biblia) {
        this.puntos_biblia = puntos_biblia;
    }

    public String getPuntos_fecha() {
        return puntos_fecha;
    }

    public void setPuntos_fecha(String puntos_fecha) {
        this.puntos_fecha = puntos_fecha;
    }

    @Override
    public String toString() {
        return "Puntos{" +
                "puntos_participacion=" + puntos_participacion +
                ", puntos_asistencia=" + puntos_asistencia +
                ", puntos_biblia=" + puntos_biblia +
                ", puntos_fecha='" + puntos_fecha + '\'' +
                '}';
    }
}
