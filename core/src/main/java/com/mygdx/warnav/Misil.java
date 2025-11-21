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
        this.bounds.y += velocidad * delta;
    }
	
	@Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }
    
    public boolean estaFueraDePantalla() {
        return this.bounds.y > 480;
    }
}
