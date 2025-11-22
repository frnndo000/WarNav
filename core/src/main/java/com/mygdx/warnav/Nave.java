package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Nave extends Entidad {
    
    // --- Sonidos y Texturas ---
    private Sound sonidoHerido;
    private Sound sonidoDisparo;
    private Sound sonidoRecarga;
    private Texture misilTexture;
    
    // --- Estadísticas Básicas ---
    private int vidas = 3;
    private int puntos = 0;
    private int velocidad = 425;
    
    // --- Estado de Daño ---
    private boolean herido = false;
    private int tiempoHeridoMax = 75; 
    private int tiempoHerido;

    // --- Munición y Disparo ---
    private Array<Misil> misiles;
    private final int MUNICION_MAXIMA = 12;
    private int municionActual;
    
    // Cooldown de disparo 
    private long lastFireTime;
    private long fireRate = 200000000L; 
    // --- POWER UPS: MUNICIÓN INFINITA ---
    private boolean municionInfinita = false;
    private float tiempoPoderMunicion = 0;

    // --- POWER UPS: PUNTOS DOBLES ---
    private boolean puntosDobles = false;
    private float tiempoPuntosDobles = 0;

    // --- POWER UPS: LÁSER DESTRUCTOR ---
    private boolean laserActivo = false;
    private float tiempoLaser = 0;
    private Rectangle laserBounds; 

    public Nave(Texture texNave, Texture texMisil, Sound ss, Sound sonidoDisparo, Sound sonidoRecarga) {
        super(texNave);
        this.misilTexture = texMisil;
        this.sonidoHerido = ss;
        this.sonidoDisparo = sonidoDisparo;
        this.sonidoRecarga = sonidoRecarga; 
    
        float nuevoAncho = 50;
        float nuevoAlto = 50;
        this.bounds.width = nuevoAncho;
        this.bounds.height = nuevoAlto;
        
        this.laserBounds = new Rectangle(0, 0, 40, 800);
    }

    public void crear() {
        bounds.x = 800 / 2 - bounds.width / 2;
        bounds.y = 20;
        misiles = new Array<Misil>();
        municionActual = MUNICION_MAXIMA;
    }

    @Override
    public void actualizar(float delta) {
        // 1. Gestionar Tiempo de Munición Infinita
        if (municionInfinita) {
            tiempoPoderMunicion -= delta;
            if (tiempoPoderMunicion <= 0) {
                municionInfinita = false;
            }
        }
        
        // 2. Gestionar Tiempo de Puntos Dobles
        if (puntosDobles) {
            tiempoPuntosDobles -= delta;
            if (tiempoPuntosDobles <= 0) {
                puntosDobles = false;
            }
        }

        // 3. Gestionar Tiempo y Posición del Láser
        if (laserActivo) {
            tiempoLaser -= delta;
            
            // El láser debe seguir a la nave
            laserBounds.x = this.bounds.x + (this.bounds.width / 2) - (laserBounds.width / 2);
            laserBounds.y = this.bounds.y + this.bounds.height; // Sale desde la punta de la nave
            
            if (tiempoLaser <= 0) {
                laserActivo = false;
            }
        }

        // 4. Movimiento de la Nave
        if (!estaHerido()) {
            procesarInput();
        }

        // 5. Actualizar Misiles
        for (int i = 0; i < misiles.size; i++) {
            Misil misil = misiles.get(i);
            misil.actualizar(delta);

            if (misil.estaFueraDePantalla()) {
                misiles.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        // Dibujar misiles normales
        for (Misil misil : misiles) {
            misil.dibujar(batch);
        }
        
        if (laserActivo) {
            batch.draw(misilTexture, laserBounds.x, laserBounds.y, laserBounds.width, laserBounds.height);
        }
        if (!herido) {
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            batch.draw(texture, bounds.x, bounds.y + MathUtils.random(-5, 5), bounds.width, bounds.height);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
    }

    public void procesarInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) bounds.x -= velocidad * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) bounds.x += velocidad * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) bounds.y += velocidad * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) bounds.y -= velocidad * Gdx.graphics.getDeltaTime();

        // Límites de pantalla
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x > 800 - bounds.width) bounds.x = 800 - bounds.width;
        if (bounds.y < 0) bounds.y = 0;
        if (bounds.y > 480 - bounds.height) bounds.y = 480 - bounds.height;
        
        if (Gdx.input.isKeyPressed(Input.Keys.J)) { 
            if (municionActual > 0 || municionInfinita) {
                disparar();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            recargar();
        }
    }

    private void disparar() {
        if (laserActivo) return;

        if (TimeUtils.nanoTime() - lastFireTime > fireRate) {
            Misil misil = new Misil(misilTexture);
            float misilX = this.bounds.x + this.bounds.width / 2 - misil.getBounds().width / 2;
            float misilY = this.bounds.y + this.bounds.height;
            misil.crear(misilX, misilY);
            misiles.add(misil);
            sonidoDisparo.play();
            if (!municionInfinita) {
                municionActual--;
            }
            
            lastFireTime = TimeUtils.nanoTime();
        }
    }
    

    public void activarMunicionInfinita() {
        municionInfinita = true;
        tiempoPoderMunicion = 5.0f;
        municionActual = MUNICION_MAXIMA; 
    }
    
    public void activarPuntosDobles() {
        puntosDobles = true;
        tiempoPuntosDobles = 10.0f; 
    }
    
    public void activarLaser() {
        laserActivo = true;
        tiempoLaser = 2.0f; 
        municionActual = MUNICION_MAXIMA; 
    }
    
    public void agregarVida() {
        vidas++;
    }

    public void sumarPuntos(int pp) {
        if (puntosDobles) {
            puntos += pp * 2;
        } else {
            puntos += pp;
        }
    }

    public void dañar() {
        vidas--;
        herido = true;
        tiempoHerido = tiempoHeridoMax;
        sonidoHerido.play();
    }

    private void recargar() {
        municionActual = MUNICION_MAXIMA;
        sonidoRecarga.play();
    }

    public void destruir() {
        super.destruir();
    }

    // --- GETTERS ---
    public int getVidas() { return vidas; }
    public int getPuntos() { return puntos; }
    public int getMunicionActual() { return municionActual; }
    public int getMunicionMaxima() { return MUNICION_MAXIMA; }
    public boolean estaHerido() { return herido; }
    public Array<Misil> getMisiles() { return misiles; }
    public boolean isLaserActivo() { return laserActivo; }
    public Rectangle getLaserBounds() { return laserBounds; }
}