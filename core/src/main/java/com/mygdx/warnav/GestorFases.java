package com.mygdx.warnav;


public class GestorFases {

    private FaseJuego faseActual;

    // Reusamos instancias, no creamos nuevas cada frame
    private final FaseJuego faseInicial   = new FaseInicial();
    private final FaseJuego faseMedia     = new FaseMedia();
    private final FaseJuego faseFinal = new FaseFinal();

    public void actualizarFaseSegunPuntos(int puntos, Nave nave, Lluvia lluvia, GameScreen screen) {
        FaseJuego nuevaFase;

        if (puntos < 1000) {
        	nuevaFase = faseInicial;
        	
        }
        else if (puntos < 5000) nuevaFase = faseMedia;
        else                   nuevaFase = faseFinal;

        if (faseActual != nuevaFase) {
        	faseActual = nuevaFase;
            faseActual.aplicarFase(nave, lluvia,screen);
        }
    }
}