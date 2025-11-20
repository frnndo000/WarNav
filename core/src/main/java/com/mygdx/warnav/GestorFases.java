package com.mygdx.warnav;

public class GestorFases {

    private FaseJuego faseActual;

    // Instancias reusables
    private final FaseJuego faseInicial = new FaseInicial();
    private final FaseJuego faseMedia   = new FaseMedia();
    private final FaseJuego faseFinal   = new FaseFinal();

    public void actualizarFaseSegunPuntos(int puntos, Nave nave, Lluvia lluvia, GameScreen screen) {
        FaseJuego nuevaFase;

        // Reglas de cambio de fase
        if (puntos < 1000) {
            nuevaFase = faseInicial;
        } 
        else if (puntos < 5000) {
            nuevaFase = faseMedia;
        } 
        else {
            nuevaFase = faseFinal;
        }

        // Si detectamos un cambio de fase...
        if (faseActual != nuevaFase) {
            faseActual = nuevaFase;
            
            // 1. Aplicar configuración estándar (Template Method)
            faseActual.aplicarFase(nave, lluvia, screen);
            
            // 2. Lógica específica para Abstract Factory en Fase Media
            if (faseActual instanceof FaseMedia) {
                ((FaseMedia) faseActual).aplicarCambioFabrica(lluvia, screen);
            }
            
            // Opcional: Si vuelves a FaseInicial (por debugging), podrías querer resetear a Fabrica Nivel 1
            // if (faseActual instanceof FaseInicial) { ... restaurar fabrica nivel 1 ... }
        }
    }
}