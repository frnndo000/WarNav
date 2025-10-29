package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Misil extends Entidad {
	private int velocidad = 600;
	
	public Misil(Texture texture) {
        super(texture);
    }
	
	public void crear(float x, float y) {
        this.bounds.x = x;
        this.bounds.y = y;
    }
	
	@Override
    public void actualizar(float delta) {
        // mueve el misil hacia arriba
        this.bounds.y += velocidad * delta;
    }
	
	@Override
    public void dibujar(SpriteBatch batch) {
        // dibuja el misil
        batch.draw(texture, bounds.x, bounds.y);
    }
    
    // metodo para saber si el misil salio de la pantalla
    public boolean estaFueraDePantalla() {
        return this.bounds.y > 480; // 480 es el alto de tu pantalla
    }
}
