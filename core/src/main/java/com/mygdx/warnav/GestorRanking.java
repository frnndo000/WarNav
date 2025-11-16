package com.mygdx.warnav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorRanking {

	private static GestorRanking inst;
    private List<UserPuntaje> puntajesAltos;
    private int MAX_ENTRADAS;

    private GestorRanking() {
        this(50);
    }

    private GestorRanking(int maxEntradas) {
        puntajesAltos = new ArrayList<>();
        this.MAX_ENTRADAS = maxEntradas;
        
        // Sembrar puntajes iniciales de NPCs espaciales
        cargarPuntajesIniciales();
    }

    public static GestorRanking getInstance() {
        if (inst == null) {
            inst = new GestorRanking();
        }
        return inst;
    }

    private void cargarPuntajesIniciales() {
        // Puedes usar agregarPuntaje para respetar el orden y el límite
        agregarPuntaje("CAPITAN ORION",      15000);
        agregarPuntaje("NOVA DRIFTER",       12000);
        agregarPuntaje("COMANDANTE VEGA",    9800);
        agregarPuntaje("ANDROMEDA",          8700);
        agregarPuntaje("LUNA ROJA",          7500);
        agregarPuntaje("ASTRO PIRATA",       6400);
        agregarPuntaje("SATURNO-7",          5200);
        agregarPuntaje("NEBULOSA X",         4100);
        agregarPuntaje("COSMO RIDER",        3500);
        agregarPuntaje("ESTRELLA FUGAZ",     2900);
    }

    public void agregarPuntaje(String nombre, int puntos) {
        UserPuntaje nuevo = new UserPuntaje(nombre, puntos);
        puntajesAltos.add(nuevo);

        // ordenar de mayor a menor
        puntajesAltos.sort((p1, p2) -> Integer.compare(p2.getPuntos(), p1.getPuntos()));

        // recortar al máximo de entradas
        if (puntajesAltos.size() > MAX_ENTRADAS) {
            puntajesAltos = new ArrayList<>(puntajesAltos.subList(0, MAX_ENTRADAS));
        }
    }

    public List<UserPuntaje> getTop10() {
        int limite = Math.min(10, puntajesAltos.size());
        return new ArrayList<>(puntajesAltos.subList(0, limite));
    }

    public UserPuntaje getSiguienteObjetivo(UserPuntaje actual) {
        if (actual == null || puntajesAltos == null || puntajesAltos.isEmpty()) {
            return null;
        }

        int puntosActual = actual.getPuntos();

        // Recorremos desde el final hacia el inicio
        for (int i = puntajesAltos.size() - 1; i >= 0; i--) {
            UserPuntaje p = puntajesAltos.get(i);
            if (p.getPuntos() > puntosActual) {
                return p;
            }
        }

        // Nadie tiene más puntos que el actual
        return null;
    }

    public List<UserPuntaje> getVecinos(UserPuntaje actual) {
        if (actual == null || puntajesAltos == null || puntajesAltos.isEmpty()) {
            return Collections.emptyList();
        }

        // Buscar por nombre + puntos (no por referencia de objeto)
        int index = -1;
        for (int i = 0; i < puntajesAltos.size(); i++) {
            UserPuntaje p = puntajesAltos.get(i);
            if (p.getNombreJugador().equals(actual.getNombreJugador())
                    && p.getPuntos() == actual.getPuntos()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return Collections.emptyList();
        }

        // Queremos: 3 arriba y 2 abajo
        int from = Math.max(0, index - 3);
        int to   = Math.min(puntajesAltos.size() - 1, index + 2);

        return new ArrayList<>(puntajesAltos.subList(from, to + 1));
    }
}
