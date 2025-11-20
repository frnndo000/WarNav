package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public interface FabricaLluvia {
    Entidad crearEnemigo();
    Entidad crearRecompensa();

    // --- NIVEL 1 (Solo Soldados) ---
    public static class Nivel1 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;

        public Nivel1(Texture texEnemigo, Texture texSoldado) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
        }
        @Override
        public Entidad crearEnemigo() { return new Enemigo(texEnemigo); }
        
        @Override
        public Entidad crearRecompensa() { return new Soldado(texSoldado); }
    }

    // --- NIVEL 2 (Soldados + Mejoras Variadas) ---
    public static class Nivel2 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;
        private Texture texMunicion;
        private Texture texVida;

        public Nivel2(Texture texEnemigo, Texture texSoldado, Texture texMunicion, Texture texVida) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
            this.texMunicion = texMunicion;
            this.texVida = texVida;
        }

        @Override
        public Entidad crearEnemigo() { return new Enemigo(texEnemigo); }

        @Override
        public Entidad crearRecompensa() {
            float random = MathUtils.random();

            if (random < 0.05f) { 
                //Probabilidad: VIDA 
                return new Mejora(texVida, Mejora.TIPO_VIDA);
                
            } else if (random < 0.10f) { 
                //Probabilidad: MUNICIÓN 
                return new Mejora(texMunicion, Mejora.TIPO_MUNICION);
                
            } else {
                // Probabilidad: SOLDADO
                return new Soldado(texSoldado);
            }
        }
    }
}