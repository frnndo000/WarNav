package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo extends Entidad {
    private EstrategiaMovimiento estrategia;

    public Enemigo(Texture texture, EstrategiaMovimiento estrategia) {
        super(texture); 
        this.estrategia = estrategia;
    }

    @Override
    public void actualizar(float delta) {
        if (estrategia != null) {
            estrategia.mover(this, delta);
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public void setEstrategia(EstrategiaMovimiento nuevaEstrategia) {
        this.estrategia = nuevaEstrategia;
    }
}