package com.mygdx.warnav;

import com.badlogic.gdx.math.Rectangle;

public interface Colisionable {
    
    /**
     * Retorna el rectángulo de colisión del objeto.
     * @return El Rectangle del objeto.
     */
    public Rectangle getBounds();
    
    // Más adelante podríamos añadir:
    // public void alColisionar(Colisionable otro);
}