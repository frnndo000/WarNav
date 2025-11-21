package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FaseFinal extends FaseJuego {

    @Override
    protected void configurarFondo(GameScreen screen) {
    	Texture tt;
		tt = new Texture(Gdx.files.internal("enemigo3.png"));
		Sprite sprite = new Sprite(tt);
		sprite.setSize(64, 64);
        screen.setFondoColor(0.15f, 0f, 0.1f);
        screen.setVelocidadEstrellas(10f, 10f, 10f);
        screen.setEnemigoTexture(tt);
    }
    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        lluvia.setProbEnemigo(0.9f);
        lluvia.setIntervaloSpawn(0.05f);
    }
    
    @Override
    protected void configurarEfectosSonido(Lluvia lluvia) {
        Music musicaBoss = Gdx.audio.newMusic(Gdx.files.internal("musica_final.mp3"));
        
        lluvia.cambiarMusica(musicaBoss);
    }
}

