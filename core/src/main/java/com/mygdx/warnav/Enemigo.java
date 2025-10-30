package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo extends Entidad {

    private int velocidad = 350;

    public Enemigo(Texture texture) {
        super(texture); 
    }

    @Override
    public void actualizar(float delta) {
        // mueve el enemigo hacia abajo
        this.bounds.y -= velocidad * delta;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        // dibuja el enemigo
        batch.draw(texture, bounds.x, bounds.y);
    }

    // metodo para saber si salio de la pantalla
    public boolean estaFueraDePantalla() {
        return this.bounds.y + this.bounds.height < 0;
    }
}