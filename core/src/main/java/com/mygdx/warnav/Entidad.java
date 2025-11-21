package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entidad implements Colisionable {
	protected Texture texture;
    protected Rectangle bounds;
    
    public Entidad(Texture texture) {
        this.texture = texture;
        this.bounds = new Rectangle();
        this.bounds.width = texture.getWidth();
        this.bounds.height = texture.getHeight();
    }
    
    
    /**
     * Actualiza la lógica de la entidad (movimiento, IA, etc.)
     * @param delta El tiempo transcurrido desde el último frame
     */
    public abstract void actualizar(float delta);

    /**
     * Dibuja la entidad en la pantalla.
     * @param batch El SpriteBatch usado para dibujar.
     */
    public abstract void dibujar(SpriteBatch batch);
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public void destruir() {
    }
}
