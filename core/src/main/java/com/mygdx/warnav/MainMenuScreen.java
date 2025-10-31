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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;


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
	
	private final GlyphLayout layout = new GlyphLayout();
	
	private void drawCentered(BitmapFont f, SpriteBatch b, String text, float y, float scale) {
	    f.getData().setScale(scale);
	    layout.setText(f, text);
	    float x = (camera.viewportWidth - layout.width) / 2f;
	    f.draw(b, text, x, y);
	}


	@Override
	public void render(float delta) {
	    ScreenUtils.clear(0.02f, 0.02f, 0.12f, 1f);

	    camera.update();
	    batch.setProjectionMatrix(camera.combined);

	    // --- layout base ---
	    final float TOP_Y = 430f;
	    final float LINE = 32f;         
	    final float GAP = 18f;          
	    final float BTN_GAP = 20f;     

	    batch.begin();

	    // 1) Título
	    drawCentered(font, batch, "Bienvenido a WarNav!!", TOP_Y, 2.0f);

	    // 2) Controles
	    float y = TOP_Y - GAP - LINE;                 

	    y -= LINE;
	    drawCentered(font, batch, "WASD  - Mover Nave", y, 1.1f);

	    y -= LINE;
	    drawCentered(font, batch, "J - Disparar   |   K - Recargar", y, 1.1f);

	    y -= LINE;
	    drawCentered(font, batch, "ESC - Pausar Juego", y, 1.1f);
	    
	    y -= (LINE + GAP);
	    drawCentered(font, batch, "CLICK PARA COMENZAR", y, 1.2f);

	    float botonX = (camera.viewportWidth - botonJugarBounds.width) / 2f;
	    float botonY = y - BTN_GAP - botonJugarBounds.height;

	    botonJugarBounds.setPosition(botonX, botonY);

	    batch.draw(botonJugarTexture, botonX, botonY, botonJugarBounds.width, botonJugarBounds.height);

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
