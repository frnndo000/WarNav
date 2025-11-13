package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Vector3; 
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen implements Screen {

    final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private Texture botonJugarTexture; 
    private Rectangle botonJugarBounds; 

    // Ranking
    private List<UserPuntaje> top10 = new ArrayList<>();

    // Utilidad para centrar texto
    private final GlyphLayout layout = new GlyphLayout();

    public MainMenuScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();  

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        botonJugarTexture = new Texture(Gdx.files.internal("boton_jugar.png")); 

        float botonAncho = 200; 
        float botonAlto = 100;  
        float botonX = (800 - botonAncho) / 2f; 
        float botonY = 80; 

        botonJugarBounds = new Rectangle(botonX, botonY, botonAncho, botonAlto);
    }

    private void drawCentered(BitmapFont f, SpriteBatch b, String text, float y, float scale) {
        f.getData().setScale(scale);
        layout.setText(f, text);
        float x = (camera.viewportWidth - layout.width) / 2f;
        f.draw(b, text, x, y);
    }

    @Override
    public void show() {
        // Cada vez que entras al menú, refresca el Top 10
        top10 = GestorRanking.getInstance().getTop10();
        if (top10 == null) {
            top10 = new ArrayList<>();
        }
    }

    @Override
    public void render(float delta) {
        // Fondo oscuro estilo arcade
        ScreenUtils.clear(0.02f, 0.02f, 0.12f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        final float TOP_Y = 440f;

        batch.begin();

        // --- TÍTULO ---
        font.setColor(Color.CYAN);
        drawCentered(font, batch, "W A R N A V", TOP_Y, 2.2f);

        // Línea bajo el título
        font.getData().setScale(1f);
        font.setColor(Color.SKY);
        layout.setText(font, "ARCADE EDITION");
        float subX = (camera.viewportWidth - layout.width) / 2f;
        font.draw(batch, "ARCADE EDITION", subX, TOP_Y - 30);

        // --- CONTROLES (lado izquierdo) ---
        float leftX = 60f;
        float yLeft = TOP_Y - 80f;

        font.getData().setScale(1.2f);
        font.setColor(Color.YELLOW);
        font.draw(batch, "CONTROLES", leftX, yLeft);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        yLeft -= 35f;
        font.draw(batch, "WASD  - Mover nave", leftX, yLeft);

        yLeft -= 25f;
        font.draw(batch, "J     - Disparar", leftX, yLeft);

        yLeft -= 25f;
        font.draw(batch, "K     - Recargar", leftX, yLeft);

        yLeft -= 25f;
        font.draw(batch, "ESC   - Pausar juego", leftX, yLeft);

        // --- TOP 10 (lado derecho) ---
        float rightX = 440f;
        float yRight = TOP_Y - 80f;

        font.getData().setScale(1.2f);
        font.setColor(Color.LIME);
        font.draw(batch, "TOP 10 JUGADORES", rightX, yRight);

        font.getData().setScale(1.0f);
        font.setColor(Color.LIGHT_GRAY);
        yRight -= 30f;

        if (top10.isEmpty()) {
            font.draw(batch, "Sin registros aún", rightX, yRight);
        } else {
            for (int i = 0; i < top10.size(); i++) {
                UserPuntaje p = top10.get(i);
                String linea = (i + 1) + ". " + p.getNombreJugador() + " - " + p.getPuntos() + " pts";
                font.draw(batch, linea, rightX, yRight);
                yRight -= 22f;
            }
        }

        // --- TEXTO DE INVITACIÓN ---
        font.getData().setScale(1.1f);
        font.setColor(Color.ORANGE);
        drawCentered(font, batch, "CLICK EN EL BOTON PARA COMENZAR", 130f + botonJugarBounds.height + 15f, 1.1f);

        // --- BOTÓN JUGAR ---
        float botonX = (camera.viewportWidth - botonJugarBounds.width) / 2f;
        float botonY = 80f;
        botonJugarBounds.setPosition(botonX, botonY);

        batch.draw(botonJugarTexture, botonX, botonY, botonJugarBounds.width, botonJugarBounds.height);

        // Reset al font para no dejar escalas locas a otras pantallas
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);

        batch.end();

        // --- INPUT ---
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
    public void resize(int width, int height) {
        // Si quieres que se adapte, puedes actualizar la cámara aquí
        // camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        botonJugarTexture.dispose(); 
    }
}

