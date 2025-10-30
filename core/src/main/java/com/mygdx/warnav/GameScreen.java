package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
	final GameLluviaMenu game;
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private Nave nave;
	private Lluvia lluvia;
	private Texture misilTexture;
	private Sound sonidoDisparo;
	private Texture soldadoTexture;
	private Texture enemigoTexture;

	   
	//boolean activo = true;

	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  // load the images for the droplet and the bucket, 64x64 pixels each 	     
		  Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
	      misilTexture = new Texture(Gdx.files.internal("misil.png"));
	      sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("disparo.mp3"));
		  nave = new Nave(new Texture(Gdx.files.internal("nave.png")), misilTexture, hurtSound, sonidoDisparo);
         
	      // load the drop sound effect and the rain background "music" 
		  soldadoTexture = new Texture(Gdx.files.internal("soldado.png")); 
		  enemigoTexture = new Texture(Gdx.files.internal("enemigo.png"));
         
         Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        
	     Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
         lluvia = new Lluvia(soldadoTexture, enemigoTexture, dropSound, rainMusic);

	      // camera
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      batch = new SpriteBatch();
	      // creacion del tarro
	      nave.crear();
	      
	      // creacion de la lluvia
	      lluvia.crear();
	}
	
	// En GameScreen.java

	private void revisarColisiones() {
	    Array<Misil> misiles = nave.getMisiles();
	    Array<Entidad> entidades = lluvia.getEntidades();

	    // Usamos bucles 'for' tradicionales para poder eliminar de forma segura
	    for (int i = 0; i < misiles.size; i++) {
	        Misil misil = misiles.get(i);

	        for (int j = 0; j < entidades.size; j++) {
	            Entidad entidad = entidades.get(j);

	            // ¿Es un enemigo?
	            if (entidad instanceof Enemigo) {

	                // ¿El misil choca con el enemigo?
	                if (misil.getBounds().overlaps(entidad.getBounds())) {

	                    // ¡Colisión!
	                    misiles.removeIndex(i);     // Elimina el misil
	                    entidades.removeIndex(j);   // Elimina al enemigo
	                    i--; // Ajusta el índice del bucle de misiles
	                    break; // El misil ya no existe, sal del bucle de enemigos
	                }
	            }
	        }
	    }
	}

	@Override
	public void render(float delta) {
		//limpia la pantalla con color azul obscuro.
		ScreenUtils.clear(0, 0, 0.2f, 1);
		//actualizar matrices de la cámara
		camera.update();
		//actualizar 
		batch.setProjectionMatrix(camera.combined);
		
		nave.actualizar(delta);
		
		if (!nave.estaHerido()) {
			// movimiento del tarro desde teclado
	        nave.actualizarMovimiento();        
			// caida de la lluvia 
	       if (!lluvia.actualizarMovimiento(nave)) {
	    	  //actualizar HigherScore
	    	  if (game.getHigherScore()<nave.getPuntos())
	    		  game.setHigherScore(nave.getPuntos());  
	    	  //ir a la ventana de finde juego y destruir la actual
	    	  game.setScreen(new GameOverScreen(game));
	    	  dispose();
	       }
		}
		
		revisarColisiones();
		
		batch.begin();
		
		//dibujar textos
		font.draw(batch, "Gotas totales: " + nave.getPuntos(), 5, 475);
		font.draw(batch, "Vidas : " + nave.getVidas(), 670, 475);
		font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		nave.dibujar(batch);
		lluvia.actualizarDibujoLluvia(batch);
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	  // continuar con sonido de lluvia
	  lluvia.continuar();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		lluvia.pausar();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
      nave.destruir();
      lluvia.destruir();
      misilTexture.dispose();
      sonidoDisparo.dispose();
      soldadoTexture.dispose();
      enemigoTexture.dispose();
	}

}
