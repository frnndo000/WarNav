package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

// Nota: Ya no necesitamos 'Rectangle', 'Lluvia' solo manejará Entidades.

public class Lluvia {
    
    // 1. Nuevas variables
    private Array<Entidad> entidades; // Una sola lista para Soldados y Enemigos
    private long lastDropTime;
    private Texture soldadoTexture; // Textura para Soldados
    private Texture enemigoTexture; // Textura para Enemigos
    private Sound dropSound; // Sonido al rescatar soldado
    private Music rainMusic; // Música de fondo
	   
    // 2. Constructor actualizado
    public Lluvia(Texture soldadoTexture, Texture enemigoTexture, Sound ss, Music mm) {
        this.rainMusic = mm;
        this.dropSound = ss;
        this.soldadoTexture = soldadoTexture;
        this.enemigoTexture = enemigoTexture;
    }
	
    public void crear() {
        entidades = new Array<Entidad>(); // Inicializa la lista
        spawnEntidad(); // Crea la primera entidad
        
        rainMusic.setLooping(true);
        rainMusic.play();
    }
	
    // 3. Método de spawn actualizado (antes 'crearGotaDeLluvia')
    private void spawnEntidad() {
        Entidad nuevaEntidad;
        
        // Decide aleatoriamente si crear un Enemigo o un Soldado
        if (MathUtils.random(1, 10) < 9) {	    	  
            nuevaEntidad = new Enemigo(enemigoTexture);
        } else {
            nuevaEntidad = new Soldado(soldadoTexture);
        }
        
        // Posición inicial (arriba, aleatorio en X)
        nuevaEntidad.getBounds().x = MathUtils.random(0, 800 - nuevaEntidad.getBounds().width);
        nuevaEntidad.getBounds().y = 480; // Alto de la pantalla
        
        entidades.add(nuevaEntidad);
        lastDropTime = TimeUtils.nanoTime();
    }
	
    // 4. Lógica de actualización (movimiento y colisiones)
    public boolean actualizarMovimiento(Nave nave) { 
        // Generar nuevas entidades
        if (TimeUtils.nanoTime() - lastDropTime > 280000000) spawnEntidad();
	  
        // Mover y revisar colisiones
        for (int i = 0; i < entidades.size; i++) {
            Entidad entidad = entidades.get(i);
            entidad.actualizar(Gdx.graphics.getDeltaTime()); // Llama al 'actualizar' de Soldado o Enemigo
	      
            // Si cae al suelo, se elimina
            if (entidad instanceof Soldado && ((Soldado) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i);
                i--; // Ajusta el índice
            } else if (entidad instanceof Enemigo && ((Enemigo) entidad).estaFueraDePantalla()) {
                entidades.removeIndex(i);
                i--; // Ajusta el índice
            }

            // Si choca con la Nave
            if (entidad.getBounds().overlaps(nave.getBounds())) {
                
                if (entidad instanceof Enemigo) { // Si es un enemigo
                    nave.dañar();
                    if (nave.getVidas() <= 0)
                        return false; // GAME OVER
                
                } else if (entidad instanceof Soldado) { // Si es un soldado (rescate)
                    nave.sumarPuntos(10);
                    dropSound.play();
                }
                
                entidades.removeIndex(i); // Elimina al soldado o enemigo
                i--;
            }
        } 
        return true; // Sigue el juego
    }
   
    // 5. Lógica de dibujo
    public void actualizarDibujoLluvia(SpriteBatch batch) { 
        // Dibuja todas las entidades (Soldados y Enemigos)
        for (Entidad entidad : entidades) {
            entidad.dibujar(batch);
        }
    }
   
    public void destruir() {
        dropSound.dispose();
        rainMusic.dispose();
        // Las texturas se liberan en GameScreen
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