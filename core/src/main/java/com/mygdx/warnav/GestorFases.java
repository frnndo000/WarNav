package com.mygdx.warnav;

public class GestorFases {

    private FaseJuego faseActual;
    
    private final FaseJuego faseInicial = new FaseInicial();
    private final FaseJuego faseMedia   = new FaseMedia();
    private final FaseJuego faseFinal   = new FaseFinal();

    public void actualizarFaseSegunPuntos(int puntos, Nave nave, Lluvia lluvia, GameScreen screen) {
        FaseJuego nuevaFase;

        if (puntos < 1500) {
            nuevaFase = faseInicial;
        } 
        else if (puntos < 3000) {
            nuevaFase = faseMedia;
        } 
        else {
            nuevaFase = faseFinal;
        }

        if (faseActual != nuevaFase) {
            faseActual = nuevaFase;
            
            faseActual.aplicarFase(nave, lluvia, screen);
            
            if (faseActual instanceof FaseMedia) {
                ((FaseMedia) faseActual).aplicarCambioFabrica(lluvia, screen);
            }
        }
    }
}