package com.mygdx.warnav;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

// 1. Aquí empieza la Interfaz (El contrato)
public interface FabricaLluvia {
    Entidad crearEnemigo();
    Entidad crearRecompensa();

    // ===========================================================
    // 2. Aquí metemos la Fábrica Nivel 1 (Clase interna estática)
    // ===========================================================
    public static class Nivel1 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;

        public Nivel1(Texture texEnemigo, Texture texSoldado) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
        }

        @Override
        public Entidad crearEnemigo() {
            return new Enemigo(texEnemigo);
        }

        @Override
        public Entidad crearRecompensa() {
            // Nivel 1: Siempre devuelve soldado (puntos)
            return new Soldado(texSoldado);
        }
    }

    // ===========================================================
    // 3. Aquí metemos la Fábrica Nivel 2 (Clase interna estática)
    // ===========================================================
    public static class Nivel2 implements FabricaLluvia {
        private Texture texEnemigo;
        private Texture texSoldado;
        private Texture texMunicion;

        public Nivel2(Texture texEnemigo, Texture texSoldado, Texture texMunicion) {
            this.texEnemigo = texEnemigo;
            this.texSoldado = texSoldado;
            this.texMunicion = texMunicion;
        }

        @Override
        public Entidad crearEnemigo() {
            return new Enemigo(texEnemigo);
        }

        @Override
        public Entidad crearRecompensa() {
            // Nivel 2: 10% probabilidad de Munición Infinita
            if (MathUtils.random() < 0.10f) {
                return new MejoraMunicion(texMunicion);
            } else {
                return new Soldado(texSoldado);
            }
        }
    }
}