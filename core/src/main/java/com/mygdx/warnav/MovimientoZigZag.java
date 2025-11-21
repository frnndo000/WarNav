package com.mygdx.warnav;

import com.badlogic.gdx.math.MathUtils;

public class MovimientoZigZag implements EstrategiaMovimiento {
    private float velocidadVertical;
    private float tiempoAcumulado = 0;
    private float amplitud = 200f;
    private float frecuencia = 5f;

    public MovimientoZigZag(float velocidadVertical) {
        this.velocidadVertical = velocidadVertical;
    }

    @Override
    public void mover(Entidad entidad, float delta) {
        tiempoAcumulado += delta;

        entidad.getBounds().y -= velocidadVertical * delta;
        float desplazamientoX = MathUtils.cos(tiempoAcumulado * frecuencia) * amplitud * delta;
        
        entidad.getBounds().x += desplazamientoX;
        
        if (entidad.getBounds().x < 0) entidad.getBounds().x = 0;
        if (entidad.getBounds().x > 800 - entidad.getBounds().width) 
            entidad.getBounds().x = 800 - entidad.getBounds().width;
    }
}