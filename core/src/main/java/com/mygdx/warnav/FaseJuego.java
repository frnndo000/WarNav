package com.mygdx.warnav;

public abstract class FaseJuego {

    public final void aplicarFase(Nave nave, Lluvia lluvia, GameScreen screen) {
        configurarFondo(screen);
        configurarEnemigos(lluvia);
        
        configurarEfectosSonido(lluvia); 
    }

    protected abstract void configurarFondo(GameScreen screen);
    protected abstract void configurarEnemigos(Lluvia lluvia);

    protected void configurarEfectosSonido(Lluvia lluvia) {
    }
}