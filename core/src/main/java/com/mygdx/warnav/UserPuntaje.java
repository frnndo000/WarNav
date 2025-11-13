package com.mygdx.warnav;

public class UserPuntaje {
    private String nombreJugador;
    private int puntos;

    public UserPuntaje(String nombreJugador, int puntos) {
        this.nombreJugador = nombreJugador;
        this.puntos = puntos;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntos() {
        return puntos;
    }
}
