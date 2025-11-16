package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FaseInicial extends FaseJuego {

	@Override
    protected void configurarFondo(GameScreen screen) {
        screen.setFondoColor(0f, 0f, 0.1f);
        screen.setVelocidadEstrellas(1f, 1f, 1f);
    }
    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        lluvia.setProbEnemigo(0.65f);
        lluvia.setIntervaloSpawn(0.125f);
    }

    @Override
    protected void configurarVelocidades(Nave nave, Lluvia lluvia) {
        lluvia.setVelocidadCaidaBase(250f);
    }

    @Override
    protected void configurarEfectosSonido() {
        // Música intensa, etc.
    }
}

