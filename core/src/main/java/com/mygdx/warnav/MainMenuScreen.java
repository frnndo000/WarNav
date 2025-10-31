package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Vector3; 
import com.badlogic.gdx.utils.ScreenUtils;


public class MainMenuScreen implements Screen {

	final GameLluviaMenu game;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;


	private Texture botonJugarTexture; 
	private Rectangle botonJugarBounds; 

	public MainMenuScreen(final GameLluviaMenu game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();  
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		botonJugarTexture = new Texture(Gdx.files.internal("boton_jugar.png")); 

		float botonAncho = 200; 
		float botonAlto = 100;  
		float botonX = (800 - botonAncho) / 2; 
		float botonY = 100; 

		botonJugarBounds = new Rectangle(botonX, botonY, botonAncho, botonAlto);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		font.getData().setScale(2, 2);
		font.draw(batch, "Bienvenido a WarNav!!! ", 100, camera.viewportHeight/2+50);
		
		batch.draw(botonJugarTexture, botonJugarBounds.x, botonJugarBounds.y, botonJugarBounds.width, botonJugarBounds.height); // <-- MODIFICADO: Dibuja el botón
		
		batch.end();

		if (Gdx.input.justTouched()) { 
			
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);

			if (botonJugarBounds.contains(touchPos.x, touchPos.y)) {
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
		botonJugarTexture.dispose(); 
	}

}
