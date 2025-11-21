package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public interface FabricaLluvia {
    Entidad crearEnemigo();
    Entidad crearRecompensa();

    // --- NIVEL 1 (Enemigos básicos y rectos) ---
    public static class Nivel1 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;

        public Nivel1(Texture texEnemigo, Texture texSoldado) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
        }
        
        @Override
        public Entidad crearEnemigo() { 
            // Aquí inyectamos la estrategia: Movimiento Recto a velocidad moderada (250)
            return new Enemigo(texEnemigo, new MovimientoRecto(250f)); 
        }
        
        @Override
        public Entidad crearRecompensa() { return new Soldado(texSoldado); }
    }

    // --- NIVEL 2 (Enemigos complejos: ZigZag o Rápidos) ---
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
        public Entidad crearEnemigo() { 
            // PATRÓN STRATEGY EN ACCIÓN:
            // Decidimos dinámicamente cómo se moverá el enemigo.
            
            if (MathUtils.randomBoolean()) {
                // 50% probabilidad: Movimiento en ZigZag
                return new Enemigo(texEnemigo, new MovimientoZigZag(200f));
            } else {
                // 50% probabilidad: Movimiento Recto pero MUY RÁPIDO (450)
                return new Enemigo(texEnemigo, new MovimientoRecto(450f));
            }
        }

        @Override
        public Entidad crearRecompensa() {
            float random = MathUtils.random();

            if (random < 0.05f) { 
                return new Mejora(texVida, Mejora.TIPO_VIDA);
            } else if (random < 0.10f) { 
                return new Mejora(texMunicion, Mejora.TIPO_MUNICION);
            } else {
                return new Soldado(texSoldado);
            }
        }
    }
}