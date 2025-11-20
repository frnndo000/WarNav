package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class FaseMedia extends FaseJuego {
    
    @Override
    protected void configurarFondo(GameScreen screen) {
        // Cambia el fondo y el enemigo visual (si tienes sprite específico)
        Texture tt = new Texture(Gdx.files.internal("enemigo2.png")); // Asegúrate de que exista o usa enemigo.png
        // Nota: Esto carga una textura nueva cada vez, idealmente usarías las de Screen,
        // pero para no romper tu lógica actual lo dejo así, o puedes usar screen.getEnemigoTexture()
        
        screen.setFondoColor(0.05f, 0.1f, 0.1f);
        screen.setVelocidadEstrellas(5f, 5f, 5f);
        
        // Opcional: cambiar la textura del enemigo en la lluvia también
        // screen.setEnemigoTexture(tt); 
    }

    @Override
    protected void configurarEnemigos(Lluvia lluvia) {
        lluvia.setProbEnemigo(0.8f);
        lluvia.setIntervaloSpawn(0.25f);
    }

    @Override
    protected void configurarVelocidades(Nave nave, Lluvia lluvia) {
        lluvia.setVelocidadCaidaBase(400f);
    }
    
    /**
     * MÉTODO NUEVO: Se llama desde GestorFases para activar el Abstract Factory Nivel 2
     */
    public void aplicarCambioFabrica(Lluvia lluvia, GameScreen screen) {
        FabricaLluvia fabricaNivel2 = new FabricaLluvia.Nivel2(
            screen.getEnemigoTexture(),
            screen.getSoldadoTexture(),
            screen.getPowerUpTexture(),
            screen.getVidaTexture()
        );
        
        lluvia.setFabrica(fabricaNivel2);
    	}
    }