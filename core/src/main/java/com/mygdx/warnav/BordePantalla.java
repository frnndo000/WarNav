package com.mygdx.warnav;

import com.badlogic.gdx.math.Rectangle;

public class BordePantalla implements Colisionable {
    
    private Rectangle bounds;

    public BordePantalla(float x, float y, float ancho, float alto) {
        this.bounds = new Rectangle(x, y, ancho, alto);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }
}