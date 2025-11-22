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
    
    // Referencia a la Fábrica Abstracta
    private FabricaLluvia fabrica;
    
    // Objeto para matar entidades que salen de pantalla
    private BordePantalla zonaMortal;
    
    private float intervaloSpawnSegundos = 0.3f;
    private float probEnemigo = 0.9f;
    private float velocidadCaidaBase = 150f;
        
    public Lluvia(FabricaLluvia fabricaInicial, Sound ss, Music mm) {
        this.rainMusic = mm;
        this.dropSound = ss;
        this.fabrica = fabricaInicial;
        
        // Crea una zona invisible abajo de la pantalla para limpiar memoria
        this.zonaMortal = new BordePantalla(0, -100, 800, 100);
    }
    
    // Método para cambiar la fábrica (Nivel 1 -> Nivel 2)
    public void setFabrica(FabricaLluvia nuevaFabrica) {
        this.fabrica = nuevaFabrica;
    }

    public void crear() {
        entidades = new Array<Entidad>();
        spawnEntidad();
        
        rainMusic.setLooping(true);
        rainMusic.play();
    }
    
    private void spawnEntidad() {
        Entidad nuevaEntidad;
        // La fábrica decide qué crear (Enemigo o Recompensa)
        if (MathUtils.random() < probEnemigo) {
            nuevaEntidad = fabrica.crearEnemigo();
        } else {
            nuevaEntidad = fabrica.crearRecompensa();
        }

        // Posición aleatoria en X, arriba en Y (480)
        nuevaEntidad.getBounds().x = MathUtils.random(0, 800 - nuevaEntidad.getBounds().width);
        nuevaEntidad.getBounds().y = 480;
        
        entidades.add(nuevaEntidad);
        lastDropTime = TimeUtils.nanoTime();
    }
    
    public boolean actualizarMovimiento(Nave nave) { 
        // 1. Generar nuevos objetos según el tiempo
        long intervaloNanos = (long) (intervaloSpawnSegundos * 1_000_000_000L);
        if (TimeUtils.nanoTime() - lastDropTime > intervaloNanos) {
            spawnEntidad();
        }

        float delta = Gdx.graphics.getDeltaTime();

        // 2. Mover y verificar colisiones
        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            
            // Actualizar posición (polimorfismo: cada entidad sabe cómo moverse)
            entidad.actualizar(delta);

            // A. Verificar si salió de la pantalla (zona mortal)
            if (entidad.getBounds().overlaps(zonaMortal.getBounds())) {
                entidades.removeIndex(i); 
                i--; 
                continue;
            }

            // B. Verificar colisión con la Nave
            if (entidad.getBounds().overlaps(nave.getBounds())) {
                
                if (entidad instanceof Enemigo) {
                    nave.dañar();
                    if (nave.getVidas() <= 0) return false; // Fin del juego
                
                } else if (entidad instanceof Soldado) { 
                    nave.sumarPuntos(100);
                    dropSound.play();
                
                } else if (entidad instanceof Mejora) {
                    // --- DETECCIÓN DE LOS 3 POWER-UPS ---
                    Mejora m = (Mejora) entidad;
                    
                    if (m.getTipo() == Mejora.TIPO_VIDA) {
                        nave.agregarVida();
                    } else if (m.getTipo() == Mejora.TIPO_MUNICION) {
                        nave.activarMunicionInfinita();
                    } else if (m.getTipo() == Mejora.TIPO_PUNTOS_DOBLES) {
                        nave.activarPuntosDobles();
                    }
                    
                    dropSound.play();
                }
                
                // Eliminar objeto tras chocar
                entidades.removeIndex(i);
                i--;
            }
        } 
        return true; // El juego continúa
    }

    public void cambiarMusica(Music nuevaMusica) {
        if (this.rainMusic != null) {
            this.rainMusic.stop();
        }
        this.rainMusic = nuevaMusica;
        this.rainMusic.setLooping(true);
        this.rainMusic.play();
    }

    public void actualizarDibujoLluvia(SpriteBatch batch) { 
        for (Entidad entidad : entidades) {
            entidad.dibujar(batch);
        }
    }
    
    // Getters y Setters de configuración
    public void setIntervaloSpawn(float segundos) { this.intervaloSpawnSegundos = segundos; }
    public void setProbEnemigo(float prob) { this.probEnemigo = MathUtils.clamp(prob, 0f, 1f); }
    public void setVelocidadCaidaBase(float vel) { this.velocidadCaidaBase = vel; }
    
    public void destruir() { 
        dropSound.dispose(); 
        rainMusic.dispose(); 
    }
    
    public void pausar() { 
        if (rainMusic.isPlaying()) rainMusic.stop(); 
    }
    
    public void continuar() { 
        rainMusic.play(); 
    }
    
    public Array<Entidad> getEntidades() { return entidades; }
}