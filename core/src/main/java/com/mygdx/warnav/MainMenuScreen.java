package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture; // <-- NUEVO: Importar Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle; // <-- NUEVO: Importar Rectangle (para los límites del botón)
import com.badlogic.gdx.math.Vector3; // <-- NUEVO: Importar Vector3 (para las coordenadas del clic)
import com.badlogic.gdx.utils.ScreenUtils;


public class MainMenuScreen implements Screen {

	final GameLluviaMenu game;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;

	// --- Variables nuevas para el botón ---
	private Texture botonJugarTexture; // <-- NUEVO: Textura para la imagen del botón
	private Rectangle botonJugarBounds; // <-- NUEVO: Los límites (el "hitbox") del botón

	public MainMenuScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch(); // <-- Línea 100% limpia
		this.font = game.getFont();   // <-- Línea 100% limpia
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// --- Cargar la textura y definir los límites del botón ---
		// Asegúrate de tener un archivo "boton_jugar.png" en tu carpeta "core/assets"
		botonJugarTexture = new Texture(Gdx.files.internal("boton_jugar.png")); 

		// --- Define la posición y el tamaño del botón ---
		float botonAncho = 200; // <-- Ajusta esto al ancho de tu imagen
		float botonAlto = 100;  // <-- Ajusta esto al alto de tu imagen
		float botonX = (800 - botonAncho) / 2; // <-- Centra el botón
		float botonY = 100; // <-- Posición Y

		botonJugarBounds = new Rectangle(botonX, botonY, botonAncho, botonAlto);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		
		// Dibujar el título
		font.getData().setScale(2, 2);
		font.draw(batch, "Bienvenido a WarNav!!! ", 100, camera.viewportHeight/2+50);
		
		// Ya no dibujamos el texto "Toca en cualquier lugar..."
		// font.draw(batch, "Toca en cualquier lugar para comenzar!", 100, camera.viewportHeight/2-50);

		// Dibujar el botón
		batch.draw(botonJugarTexture, botonJugarBounds.x, botonJugarBounds.y, botonJugarBounds.width, botonJugarBounds.height); // <-- MODIFICADO: Dibuja el botón
		
		batch.end();

		// --- Lógica de clic ---
		
		if (Gdx.input.justTouched()) { // Revisa si la pantalla se acaba de tocar (una sola vez)
			
			// Obtiene las coordenadas del clic
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // <-- NUEVO: Convierte las coordenadas del clic a las coordenadas del juego/cámara
			
			// Revisa si las coordenadas del clic están DENTRO de los límites del botón
			if (botonJugarBounds.contains(touchPos.x, touchPos.y)) {
				// ¡Sí! El usuario tocó el botón.
				game.setScreen(new GameScreen(game));
				dispose();
			}
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// Es importante liberar la memoria de las texturas que cargamos
		botonJugarTexture.dispose(); // <-- NUEVO
	}

}
