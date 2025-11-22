package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public interface FabricaLluvia {
    Entidad crearEnemigo();
    Entidad crearRecompensa();

    public static class Nivel1 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;

        public Nivel1(Texture texEnemigo, Texture texSoldado) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
        }
        
        @Override
        public Entidad crearEnemigo() { 
            return new Enemigo(texEnemigo, new MovimientoRecto(250f)); 
        }
        
        @Override
        public Entidad crearRecompensa() { return new Soldado(texSoldado); }
    }
    
    public static class Nivel2 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;
        private Texture texMunicion;
        private Texture texVida;
        private Texture texPuntos; 

        public Nivel2(Texture texEnemigo, Texture texSoldado, Texture texMunicion, Texture texVida, Texture texPuntos) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
            this.texMunicion = texMunicion;
            this.texVida = texVida;
            this.texPuntos = texPuntos; 
        }

        @Override
        public Entidad crearEnemigo() { 
            if (MathUtils.randomBoolean()) {
                return new Enemigo(texEnemigo, new MovimientoZigZag(200f));
            } else {
                return new Enemigo(texEnemigo, new MovimientoRecto(450f));
            }
        }

        @Override
        public Entidad crearRecompensa() {
            float random = MathUtils.random();

            if (random < 0.10f) { 
                // 10% Vida
                return new Mejora(texVida, Mejora.TIPO_VIDA);
                
            } else if (random < 0.25f) { 
                // 15% Munición
                return new Mejora(texMunicion, Mejora.TIPO_MUNICION);
                
            } else if (random < 0.40f) {
                // 15% PUNTOS DOBLES
                return new Mejora(texPuntos, Mejora.TIPO_PUNTOS_DOBLES);
                
            } else {
                // 60% Soldado
                return new Soldado(texSoldado);
            }
        }
    }
}