package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Pixmap;

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

    // ===== Fondo de estrellas =====
    static class Star { float x, y, speed, size; }
    private Texture starTex;
    private Star[] starsFar, starsMid, starsNear;

    // ===== Ranking / objetivo =====
    private GestorRanking gestorRanking;
    private UserPuntaje objetivoActual;   // siguiente jugador a superar

    private Star[] createStarLayer(int count, float minSpeed, float maxSpeed, float minSize, float maxSize) {
        Star[] arr = new Star[count];
        for (int i = 0; i < count; i++) {
            Star s = new Star();
            s.x = (float)Math.random() * camera.viewportWidth;
            s.y = (float)Math.random() * camera.viewportHeight;
            s.speed = minSpeed + (float)Math.random() * (maxSpeed - minSpeed);
            s.size  = minSize  + (float)Math.random() * (maxSize  - minSize);
            arr[i] = s;
        }
        return arr;
    }

    private void updateAndDrawStars(Star[] layer, float speedScale) {
        for (Star s : layer) {
            s.y -= s.speed * speedScale * Gdx.graphics.getDeltaTime() * 60f; // normaliza a ~60fps
            if (s.y < -2) {
                s.y = camera.viewportHeight + 2;
                s.x = (float)Math.random() * camera.viewportWidth;
            }
            batch.draw(starTex, s.x, s.y, s.size, s.size);
        }
    }

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

        // Fondo estelar
        Pixmap pm = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pm.setColor(1, 1, 1, 1);
        pm.fill();
        starTex = new Texture(pm);
        pm.dispose();

        starsFar  = createStarLayer(120, 0.15f, 0.35f, 0.8f, 1.2f);
        starsMid  = createStarLayer(80,  0.40f, 0.80f, 1.0f, 1.8f);
        starsNear = createStarLayer(50,  0.60f, 0.90f, 1.4f, 2.2f);

        // ===== Singleton del ranking =====
        gestorRanking = GestorRanking.getInstance();
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
                // Mantienes tu lógica actual de HighScore
                if (game.getHigherScore() < nave.getPuntos())
                    game.setHigherScore(nave.getPuntos());

                // (El registro en GestorRanking se hará en GameOverScreen)
                game.setScreen(new GameOverScreen(game, nave.getPuntos()));
                dispose();
                return;
            }
        }

        revisarColisiones();

        // ===== ACTUALIZAR OBJETIVO DEL RANKING =====
        // Creamos un UserPuntaje "temporal" con tus puntos actuales.
        // getSiguienteObjetivo solo usa los puntos, así que sirve.
        objetivoActual = gestorRanking.getSiguienteObjetivo(
                new UserPuntaje("JugadorActual", nave.getPuntos())
        );

        batch.begin();

        // Fondo estrellas
        updateAndDrawStars(starsFar,  0.75f);
        updateAndDrawStars(starsMid,  0.5f);
        updateAndDrawStars(starsNear, 1f);

        // HUD principal
        font.setColor(Color.WHITE);
        font.getData().setScale(1.0f);
        font.draw(batch, "Puntos: " + nave.getPuntos(), 5, 475);
        font.draw(batch, "Vidas : " + nave.getVidas(), 670, 475);
        font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth / 2f - 60f, 475);
        font.draw(batch, "Munición: " + nave.getMunicionActual() + " / " + nave.getMunicionMaxima(), 5, 25);

        // ===== HUD de objetivo arcade (ranking) =====
        font.setColor(Color.CYAN);
        font.getData().setScale(0.95f);

        if (objetivoActual != null) {
            String textoObjetivo = "Objetivo: " + objetivoActual.getNombreJugador()
                    + " (" + objetivoActual.getPuntos() + " pts)";
            font.draw(batch, textoObjetivo, 5, 450);
        } else {
            // No hay nadie por encima → eres Nº1
            font.draw(batch, "Objetivo: ¡Ya eres #1 del ranking!", 5, 450);
        }

        // Dibujar nave y entidades
        nave.dibujar(batch);
        lluvia.actualizarDibujoLluvia(batch);

        // Reset estilo fuente para no contaminar otras pantallas
        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);

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
        // Entidades
        nave.destruir();
        lluvia.destruir();

        // Texturas
        misilTexture.dispose();
        soldadoTexture.dispose();
        enemigoTexture.dispose();
        naveTexture.dispose();
        if (starTex != null) starTex.dispose();

        // Sonidos/Música
        sonidoDisparo.dispose();
        hurtSound.dispose();
        sonidoRecarga.dispose();
        sonidoRescate.dispose();
        rainMusic.dispose();
    }
}
