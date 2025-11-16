package com.mygdx.warnav;

public abstract class FaseJuego {

	    // TEMPLATE METHOD
	    public final void aplicarFase(Nave nave, Lluvia lluvia, GameScreen screen) {
	        configurarFondo(screen);
	        configurarEnemigos(lluvia);
	        configurarVelocidades(nave, lluvia);
	        configurarEfectosSonido();
	    }

	    protected abstract void configurarFondo(GameScreen screen);
	    protected abstract void configurarEnemigos(Lluvia lluvia);
	    protected abstract void configurarVelocidades(Nave nave, Lluvia lluvia);

	    protected void configurarEfectosSonido() {
	        // por defecto nada
	    }
}



