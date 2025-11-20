package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
    
    private Array<Entidad> entidades; 
    private long lastDropTime;
    private Sound dropSound;
    private Music rainMusic;
    
    // AHORA USAMOS LA FÁBRICA, NO TEXTURAS SUELTAS
    private FabricaLluvia fabrica;
    
    private float intervaloSpawnSegundos = 0.3f;
    private float probEnemigo = 0.9f;
    private float velocidadCaidaBase = 150f;
        
    public Lluvia(FabricaLluvia fabricaInicial, Sound ss, Music mm) {
        this.rainMusic = mm;
        this.dropSound = ss;
        this.fabrica = fabricaInicial; // Guardamos la fábrica
    }
    
    public void crear() {
        entidades = new Array<Entidad>();
        spawnEntidad();
        
        rainMusic.setLooping(true);
        rainMusic.play();
    }
    
    private void spawnEntidad() {
        Entidad nuevaEntidad;
        // La fábrica decide qué crear exactamente
        if (MathUtils.random() < probEnemigo) {
            nuevaEntidad = fabrica.crearEnemigo();
        } else {
        	nuevaEntidad = fabrica.crearRecompensa();
        }

        nuevaEntidad.getBounds().x = MathUtils.random(0, 800 - nuevaEntidad.getBounds().width);
        nuevaEntidad.getBounds().y = 480;
        
        entidades.add(nuevaEntidad);
        lastDropTime = TimeUtils.nanoTime();
    }
    
    public boolean actualizarMovimiento(Nave nave) { 
        long intervaloNanos = (long) (intervaloSpawnSegundos * 1_000_000_000L);
        if (TimeUtils.nanoTime() - lastDropTime > intervaloNanos) {
            spawnEntidad();
        }

        float delta = Gdx.graphics.getDeltaTime();

        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            entidad.getBounds().y -= velocidadCaidaBase * delta;

            // Check fuera de pantalla
            if (entidad instanceof Soldado && ((Soldado) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i); i--; continue;
            } else if (entidad instanceof Enemigo && ((Enemigo) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i); i--; continue;
            } else if (entidad instanceof MejoraMunicion && ((MejoraMunicion) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i); i--; continue;
            }

            // Colisiones
            if (entidad.getBounds().overlaps(nave.getBounds())) {
                
                if (entidad instanceof Enemigo) {
                    nave.dañar();
                    if (nave.getVidas() <= 0) return false; 
                
                } else if (entidad instanceof Soldado) { 
                    nave.sumarPuntos(100);
                    dropSound.play();
                
                } else if (entidad instanceof MejoraMunicion) {
                    // --- AQUI SE ACTIVA EL PODER ---
                    nave.activarMunicionInfinita();
                    dropSound.play(); // O pon otro sonido si quieres
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
    
    // Getters y Setters necesarios
    public void setIntervaloSpawn(float segundos) { this.intervaloSpawnSegundos = segundos; }
    public void setProbEnemigo(float prob) { this.probEnemigo = MathUtils.clamp(prob, 0f, 1f); }
    public void setVelocidadCaidaBase(float vel) { this.velocidadCaidaBase = vel; }
    public void setFabrica(FabricaLluvia nuevaFabrica) { this.fabrica = nuevaFabrica; }
    public void destruir() { dropSound.dispose(); rainMusic.dispose(); }
    public void pausar() { rainMusic.stop(); }
    public void continuar() { rainMusic.play(); }
    public Array<Entidad> getEntidades() { return entidades; }
}