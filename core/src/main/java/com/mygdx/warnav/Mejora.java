package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mejora extends Entidad {
    public static final int TIPO_MUNICION = 1;
    public static final int TIPO_VIDA = 2;
    public static final int TIPO_PUNTOS_DOBLES = 3;

    private int tipo;
    private int velocidad = 200; 

    public Mejora(Texture texture, int tipo) {
        super(texture);
        this.tipo = tipo;
        this.bounds.width = 40;
        this.bounds.height = 40;
        
        if (tipo == TIPO_VIDA) {
            this.velocidad = 250;
        }
    }

    @Override
    public void actualizar(float delta) {
        this.bounds.y -= velocidad * delta;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
    
    public int getTipo() {
        return tipo;
    }
}
