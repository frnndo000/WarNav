package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo extends Entidad {
    private EstrategiaMovimiento estrategia;

    // ahora pide la estrategia
    public Enemigo(Texture texture, EstrategiaMovimiento estrategia) {
        super(texture); 
        this.estrategia = estrategia;
    }

    @Override
    public void actualizar(float delta) {
        // le pedimos a la estrategia que mueva a este enemigo
        if (estrategia != null) {
            estrategia.mover(this, delta);
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public boolean estaFueraDePantalla() {
        return this.bounds.y + this.bounds.height < 0;
    }
    
    // Setter por si queremos cambiar la estrategia en pleno vuelo
    public void setEstrategia(EstrategiaMovimiento nuevaEstrategia) {
        this.estrategia = nuevaEstrategia;
    }
}