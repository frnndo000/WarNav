package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

public class Nave extends Entidad {
	   private Sound sonidoHerido;
	   private int vidas = 3;
	   private int puntos = 0;
	   private int velocidad = 400;
	   private boolean herido = false;
	   private int tiempoHeridoMax=50;
	   private int tiempoHerido;
	   private Array<Misil> misiles; 
	   private Texture misilTexture;
	   private Sound sonidoDisparo;
	   
	   public Nave(Texture texNave, Texture texMisil, Sound ss, Sound sonidoDisparo) {
		    super(texNave); 
		    this.misilTexture = texMisil; 
		    this.sonidoHerido = ss;
		    this.sonidoDisparo = sonidoDisparo;
	   }
	   
	   public void crear() {
	        bounds.x = 800 / 2 - bounds.width / 2;
	        bounds.y = 20;
	        misiles = new Array<Misil>();
	    }
	   
	   @Override
	   public void actualizar(float delta) {
	       if (!estaHerido()) {
	           actualizarMovimiento();
	       }

	       for (int i = 0; i < misiles.size; i++) {
	           Misil misil = misiles.get(i);
	           misil.actualizar(delta);

	           // se elimina si sale de la pantalla
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
	            batch.draw(texture, bounds.x, bounds.y);
	        } else {
	            // efecto de herido
	            batch.draw(texture, bounds.x, bounds.y + MathUtils.random(-5, 5));
	            tiempoHerido--;
	            if (tiempoHerido <= 0) herido = false;
	        }
	    } 
	   
	   public void actualizarMovimiento() { 
	        //movimiento desde teclado
	        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bounds.x -= velocidad * Gdx.graphics.getDeltaTime();
	        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bounds.x += velocidad * Gdx.graphics.getDeltaTime();
	        
	        //movimiento vertical para la nave
	        if(Gdx.input.isKeyPressed(Input.Keys.UP)) bounds.y += velocidad * Gdx.graphics.getDeltaTime();
	        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) bounds.y -= velocidad * Gdx.graphics.getDeltaTime();

	        // que no se salga de los bordes izq y der
	        if(bounds.x < 0) bounds.x = 0;
	        if(bounds.x > 800 - bounds.width) bounds.x = 800 - bounds.width;
	        
	        // lmites verticales
	        if(bounds.y < 0) bounds.y = 0;
	        if(bounds.y > 480 - bounds.height) bounds.y = 480 - bounds.height;
	        
	        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
	            disparar();
	        }
	    }
	   
		public int getVidas() {
			return vidas;
		}
	
		public int getPuntos() {
			return puntos;
		}

		public Rectangle getArea() {
			return bounds;
		}
		
		public void sumarPuntos(int pp) {
			puntos+=pp;
		}
	
	   public void dañar() {
		  vidas--;
		  herido = true;
		  tiempoHerido=tiempoHeridoMax;
		  sonidoHerido.play();
	   }
	   
	   private void disparar() {
		    // 1. Crea un nuevo misil
		    Misil misil = new Misil(misilTexture);

		    // 2. Calcula dónde debe aparecer (centrado, arriba de la nave)
		    float misilX = this.bounds.x + this.bounds.width / 2 - misil.getBounds().width / 2;
		    float misilY = this.bounds.y + this.bounds.height;

		    // 3. Posiciona el misil
		    misil.crear(misilX, misilY);

		    // 4. Añádelo a la lista y reproduce el sonido
		    misiles.add(misil);
		    sonidoDisparo.play();
		}

	   public void destruir() {
	        super.destruir();
	        sonidoHerido.dispose(); 
	    }
	
		public boolean estaHerido() {
			return herido;
		}
	   
}
