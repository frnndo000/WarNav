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
	private Texture soldadoTexture;
	private Texture enemigoTexture;
	private Texture naveTexture; 
	private Sound sonidoDisparo;
	private Sound hurtSound;
	private Sound sonidoRecarga;
	private Sound sonidoRescate;
	private Music rainMusic;


	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
		
		misilTexture = new Texture(Gdx.files.internal("misil.png"));
		soldadoTexture = new Texture(Gdx.files.internal("soldado.png"));
		enemigoTexture = new Texture(Gdx.files.internal("enemigo.png"));
		
		naveTexture = new Texture(Gdx.files.internal("nave.png")); 
		
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("disparo.wav")); 
		sonidoRescate = Gdx.audio.newSound(Gdx.files.internal("rescate.mp3"));
		sonidoRecarga = Gdx.audio.newSound(Gdx.files.internal("recarga.wav"));
		
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		nave = new Nave(naveTexture, misilTexture, hurtSound, sonidoDisparo, sonidoRecarga);
		
		lluvia = new Lluvia(soldadoTexture, enemigoTexture, sonidoRescate, rainMusic);

		// camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		// creacion de la nave y la lluvia
		nave.crear();
		lluvia.crear();
	}
	
	private void revisarColisiones() {
		Array<Misil> misiles = nave.getMisiles();
		Array<Entidad> entidades = lluvia.getEntidades();

		for (int i = 0; i < misiles.size; i++) {
			Misil misil = misiles.get(i);
			for (int j = 0; j < entidades.size; j++) {
				Entidad entidad = entidades.get(j);
				if (entidad instanceof Enemigo) {
					if (misil.getBounds().overlaps(entidad.getBounds())) {
						misiles.removeIndex(i);
						entidades.removeIndex(j);
						i--; 
						break;
					}
				}
			}
		}
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		nave.actualizar(delta);
		
		if (!nave.estaHerido()) {
			if (!lluvia.actualizarMovimiento(nave)) {
				if (game.getHigherScore()<nave.getPuntos())
					game.setHigherScore(nave.getPuntos());
				game.setScreen(new GameOverScreen(game));
				dispose();
			}
		}
		
		revisarColisiones();
		
		batch.begin();
		
		//dibujar textos
		font.draw(batch, "Puntos: " + nave.getPuntos(), 5, 475);
		font.draw(batch, "Vidas : " + nave.getVidas(), 670, 475);
		font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		font.draw(batch, "Munición: " + nave.getMunicionActual() + " / " + nave.getMunicionMaxima(), 5, 25);
		
		nave.dibujar(batch);
		lluvia.actualizarDibujoLluvia(batch);
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
		// Destruimos la nave y la lluvia
		nave.destruir();
		lluvia.destruir();
		
		// Destruimos las texturas
		misilTexture.dispose();
		soldadoTexture.dispose();
		enemigoTexture.dispose();
		
		// Destruir la textura de la nave
		naveTexture.dispose(); 
		
		// Destruimos los sonidos
		sonidoDisparo.dispose();
		hurtSound.dispose();
		sonidoRecarga.dispose();
		sonidoRescate.dispose();
		rainMusic.dispose();
	}
}