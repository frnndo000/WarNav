package com.mygdx.warnav; // <--- IMPORTANTE: Que esto coincida

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MejoraMunicion extends Entidad { // <--- IMPORTANTE: extends Entidad
    private int velocidad = 200;

    public MejoraMunicion(Texture texture) {
        super(texture);
        this.bounds.width = 45;
        this.bounds.height = 45;
    }

    @Override
    public void actualizar(float delta) {
        this.bounds.y -= velocidad * delta;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
    
    public boolean estaFueraDePantalla() {
         return this.bounds.y + this.bounds.height < 0;
    }
}