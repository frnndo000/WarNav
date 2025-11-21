package com.mygdx.warnav;

public class MovimientoRecto implements EstrategiaMovimiento {
    private float velocidad;

    public MovimientoRecto(float velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public void mover(Entidad entidad, float delta) {
        entidad.getBounds().y -= velocidad * delta;
    }
}