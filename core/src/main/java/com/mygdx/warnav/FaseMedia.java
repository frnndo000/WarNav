package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FaseMedia extends FaseJuego {
    
	@Override
    protected void configurarFondo(GameScreen screen) {
		Texture tt;
		tt = new Texture(Gdx.files.internal("enemigo2.png"));
		Sprite sprite = new Sprite(tt);
		sprite.setSize(64, 64);
    	screen.setFondoColor(0.05f, 0.1f, 0.1f);
        screen.setVelocidadEstrellas(5f,5f,5f);
        screen.setEnemigoTexture(tt);
    }

    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        lluvia.setProbEnemigo(0.8f);
        lluvia.setIntervaloSpawn(0.1f);
    }

    @Override
    protected void configurarVelocidades(Nave nave, Lluvia lluvia) {
        lluvia.setVelocidadCaidaBase(400f);
    }
}
