package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
    
    private Array<Entidad> entidades; 
    private long lastDropTime;
    private Texture soldadoTexture; 
    private Texture enemigoTexture; 
    private Sound dropSound;
    private Music rainMusic;
    private float intervaloSpawnSegundos = 0.28f;  // antes estaba "quemado" como 280ms
    private float probEnemigo = 0.9f;
    private float velocidadCaidaBase = 150f;
	   
    public Lluvia(Texture soldadoTexture, Texture enemigoTexture, Sound ss, Music mm) {
        this.rainMusic = mm;
        this.dropSound = ss;
        this.soldadoTexture = soldadoTexture;
        this.enemigoTexture = enemigoTexture;
    }
	
    public void crear() {
        entidades = new Array<Entidad>();
        spawnEntidad();
        
        rainMusic.setLooping(true);
        rainMusic.play();
    }
	
    private void spawnEntidad() {
        Entidad nuevaEntidad;
        if (MathUtils.random() < probEnemigo) {
            nuevaEntidad = new Enemigo(enemigoTexture);
        } else {
            nuevaEntidad = new Soldado(soldadoTexture);
        }

        nuevaEntidad.getBounds().x = MathUtils.random(0, 800 - nuevaEntidad.getBounds().width);
        nuevaEntidad.getBounds().y = 480;
        
        entidades.add(nuevaEntidad);
        lastDropTime = TimeUtils.nanoTime();
    }
	
    public boolean actualizarMovimiento(Nave nave) { 
    	//intervaloSpawnSegundos para gestor de fases
    	long intervaloNanos = (long) (intervaloSpawnSegundos * 1_000_000_000L);
    	if (TimeUtils.nanoTime() - lastDropTime > intervaloNanos) {
    	    spawnEntidad();
    	}

    	float delta = Gdx.graphics.getDeltaTime();

    	for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);

            // 🔹 Movimiento vertical usando velocidadCaidaBase
            entidad.getBounds().y -= velocidadCaidaBase * delta;

            // Eliminar si se sale de pantalla
            if (entidad instanceof Soldado && ((Soldado) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i);
                i--; 
                continue;
            } else if (entidad instanceof Enemigo && ((Enemigo) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i);
                i--;
                continue;
            }

            // Colisión con la nave
            if (entidad.getBounds().overlaps(nave.getBounds())) {
                
                if (entidad instanceof Enemigo) {
                    nave.dañar();
                    if (nave.getVidas() <= 0)
                        return false; 
                
                } else if (entidad instanceof Soldado) { 
                    nave.sumarPuntos(100);
                    dropSound.play();
                }
                
                entidades.removeIndex(i);
                i--;
            }
        } 
        return true;
    }

    public void actualizarDibujoLluvia(SpriteBatch batch) { 
        for (Entidad entidad : entidades) {
            entidad.dibujar(batch);
        }
    }
   
    public void setIntervaloSpawn(float segundos) {
        this.intervaloSpawnSegundos = segundos;
    }

    public void setProbEnemigo(float prob) {
        this.probEnemigo = MathUtils.clamp(prob, 0f, 1f);
    }
    
    public void setVelocidadCaidaBase(float vel) {
        this.velocidadCaidaBase = vel;
    }
    
    public void setEnemigoTexture(Texture tex) {
        this.enemigoTexture = tex;
    }

    
    public void destruir() {
        dropSound.dispose();
        rainMusic.dispose();
    }
   
    public void pausar() {
        rainMusic.stop();
    }
   
    public void continuar() {
        rainMusic.play();
    }
    
    public Array<Entidad> getEntidades() {
        return entidades;
    }
}