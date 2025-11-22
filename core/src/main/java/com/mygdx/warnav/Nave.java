package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Nave extends Entidad {
	private Sound sonidoHerido;
	private int vidas = 3;
	private int puntos = 0;
	private int velocidad = 425;
	private boolean herido = false;
	private int tiempoHeridoMax = 75;
	private int tiempoHerido;
	private Array<Misil> misiles;
	private Texture misilTexture;
	private final int MUNICION_MAXIMA = 12;
	private boolean municionInfinita = false;
	private float tiempoPoder = 0;
	private boolean puntosDobles = false;
	private float tiempoPuntosDobles = 0;
	private int municionActual;
	private Sound sonidoDisparo;
	private Sound sonidoRecarga; 
	
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
	}

	public void crear() {
		bounds.x = 800 / 2 - bounds.width / 2;
		bounds.y = 20;
		misiles = new Array<Misil>();
		municionActual = MUNICION_MAXIMA;
	}

	@Override
	public void actualizar(float delta) {
	    if (municionInfinita) {
	        tiempoPoder -= delta;
	        if (tiempoPoder <= 0) {
	            municionInfinita = false;
	            System.out.println("¡Munición infinita terminada!");
	        }
	    if (puntosDobles) {
	    	tiempoPuntosDobles -= delta;
	        if (tiempoPuntosDobles <= 0) {
	            puntosDobles = false;
	            System.out.println("¡Fin de Puntos Dobles!");
	            }
	        }    
	    }

	    if (!estaHerido()) {
	    	procesarInput();
	    }

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
		for (Misil misil : misiles) {
			misil.dibujar(batch);
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

		if (bounds.x < 0) bounds.x = 0;
		if (bounds.x > 800 - bounds.width) bounds.x = 800 - bounds.width;
		if (bounds.y < 0) bounds.y = 0;
		if (bounds.y > 480 - bounds.height) bounds.y = 480 - bounds.height;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
			if (municionActual > 0) {
				disparar();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
			recargar();
		}
	}

	public int getVidas() {
		return vidas;
	}

	public int getPuntos() {
		return puntos;
	}
	
	public int getMunicionActual() {
		return municionActual;
	}
	
	public int getMunicionMaxima() {
		return MUNICION_MAXIMA;
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

	private void disparar() {
	    Misil misil = new Misil(misilTexture);
	    float misilX = this.bounds.x + this.bounds.width / 2 - misil.getBounds().width / 2;
	    float misilY = this.bounds.y + this.bounds.height;
	    misil.crear(misilX, misilY);
	    misiles.add(misil);
	    sonidoDisparo.play();
	    
	    if (!municionInfinita) {
	        municionActual--;
	    }
	}
	
	public void activarMunicionInfinita() {
	    municionInfinita = true;
	    tiempoPoder = 7.0f;
	    municionActual = MUNICION_MAXIMA; 
	}
	
	public void activarPuntosDobles() {
	    puntosDobles = true;
	    tiempoPuntosDobles = 10.0f; 
	}
	
	private void recargar() {
		municionActual = MUNICION_MAXIMA;
		sonidoRecarga.play();
	}
	
	public void agregarVida() {
	    vidas++;
	}

	public void destruir() {
		super.destruir();
	}

	public boolean estaHerido() {
		return herido;
	}

	public Array<Misil> getMisiles() {
		return misiles;
	}
}