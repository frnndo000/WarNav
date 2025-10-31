package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Soldado extends Entidad {

    private int velocidad = 200;

    public Soldado(Texture texture) {
    	super(texture);

    	float nuevoAncho = 32;
    	float nuevoAlto = 32;
    	this.bounds.width = nuevoAncho;
    	this.bounds.height = nuevoAlto;
    }

    @Override
    public void actualizar(float delta) {
        // mueve el soldado hacia abajo
        this.bounds.y -= velocidad * delta;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
    	batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
    
    // metodo para saber si salio de la pantalla
    public boolean estaFueraDePantalla() {
        // 480 es el alto, 0 es el bajo
        return this.bounds.y + this.bounds.height < 0; 
    }
}