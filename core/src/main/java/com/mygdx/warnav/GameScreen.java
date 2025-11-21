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
    private GestorFases gestorFases;
    private GestorRanking gestorRanking;

    private Texture misilTexture;
    private Texture soldadoTexture;
    private Texture enemigoTexture;
    private Texture naveTexture; 
    private Texture powerUpTexture; 
    private Texture vidaTexture;

    private Sound sonidoDisparo;
    private Sound hurtSound;
    private Sound sonidoRecarga;
    private Sound sonidoRescate;
    private Music rainMusic;
    private Sound sonidoExplosion;

    private float bgR = 0f, bgG = 0f, bgB = 0.2f;   

    static class Star { float x, y, speed, size; }
    private Texture starTex;
    private Star[] starsFar, starsMid, starsNear;
    private float starsFarSpeed  = 0.75f;
    private float starsMidSpeed  = 0.5f;
    private float starsNearSpeed = 1.0f;

    private UserPuntaje objetivoActual;

    public GameScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        misilTexture = new Texture(Gdx.files.internal("misil.png"));
        soldadoTexture = new Texture(Gdx.files.internal("soldado.png"));
        enemigoTexture = new Texture(Gdx.files.internal("enemigo.png"));
        naveTexture = new Texture(Gdx.files.internal("nave.png"));
        powerUpTexture = new Texture(Gdx.files.internal("MAXAMO.png"));
        vidaTexture = new Texture(Gdx.files.internal("vida.png"));
        
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("disparo.wav")); 
        sonidoRescate = Gdx.audio.newSound(Gdx.files.internal("rescate.mp3"));
        sonidoRecarga = Gdx.audio.newSound(Gdx.files.internal("recarga.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        sonidoExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        nave = new Nave(naveTexture, misilTexture, hurtSound, sonidoDisparo, sonidoRecarga);
        nave.crear();

        FabricaLluvia fabricaInicial = new FabricaLluvia.Nivel1(enemigoTexture, soldadoTexture);
        lluvia = new Lluvia(fabricaInicial, sonidoRescate, rainMusic);
        lluvia.crear();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        crearEstrellas();

        gestorRanking = GestorRanking.getInstance();
        gestorFases = new GestorFases();
    }

    public Texture getEnemigoTexture() { return enemigoTexture; }
    public Texture getSoldadoTexture() { return soldadoTexture; }
    public Texture getPowerUpTexture() { return powerUpTexture; }
    public Texture getVidaTexture() { return vidaTexture; }
    
    private void crearEstrellas() {
        Pixmap pm = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pm.setColor(1, 1, 1, 1);
        pm.fill();
        starTex = new Texture(pm);
        pm.dispose();

        starsFar  = createStarLayer(120, 0.15f, 0.35f, 0.8f, 1.2f);
        starsMid  = createStarLayer(80,  0.40f, 0.80f, 1.0f, 1.8f);
        starsNear = createStarLayer(50,  0.60f, 0.90f, 1.4f, 2.2f);
    }

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
            s.y -= s.speed * speedScale * Gdx.graphics.getDeltaTime() * 60f; 
            if (s.y < -2) {
                s.y = camera.viewportHeight + 2;
                s.x = (float)Math.random() * camera.viewportWidth;
            }
            batch.draw(starTex, s.x, s.y, s.size, s.size);
        }
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
                        sonidoExplosion.play();
                        nave.sumarPuntos(1);
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
        ScreenUtils.clear(bgR, bgG, bgB, 1f);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        nave.actualizar(delta);

        if (!nave.estaHerido()) {
            if (!lluvia.actualizarMovimiento(nave)) {
                if (game.getHigherScore() < nave.getPuntos())
                    game.setHigherScore(nave.getPuntos());
                
                game.setScreen(new GameOverScreen(game, nave.getPuntos()));
                dispose();
                return;
            }
        }
        
        gestorFases.actualizarFaseSegunPuntos(nave.getPuntos(), nave, lluvia, this);

        revisarColisiones();

        objetivoActual = gestorRanking.getSiguienteObjetivo(
                new UserPuntaje("JugadorActual", nave.getPuntos())
        );

        batch.begin();

        updateAndDrawStars(starsFar,  starsFarSpeed);
        updateAndDrawStars(starsMid,  starsMidSpeed);
        updateAndDrawStars(starsNear, starsNearSpeed);

        font.setColor(Color.WHITE);
        font.getData().setScale(1.0f);
        font.draw(batch, "Puntos: " + nave.getPuntos(), 5, 475);
        font.draw(batch, "Vidas : " + nave.getVidas(), 670, 475);
        font.draw(batch, "Munición: " + nave.getMunicionActual() + " / " + nave.getMunicionMaxima(), 5, 25);

        font.setColor(Color.CYAN);
        font.getData().setScale(0.95f);
        if (objetivoActual != null) {
            String textoObjetivo = "Objetivo: " + objetivoActual.getNombreJugador() 
                    + " (" + objetivoActual.getPuntos() + " pts)";
            font.draw(batch, textoObjetivo, 5, 450);
        } else {
            font.draw(batch, "Objetivo: ¡Ya eres #1 del ranking!", 5, 450);
        }

        nave.dibujar(batch);
        lluvia.actualizarDibujoLluvia(batch);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        batch.end();
    }
    
    public void setFondoColor(float r, float g, float b) {
        this.bgR = r; this.bgG = g; this.bgB = b;
    }
    
    public void setVelocidadEstrellas(float far, float mid, float near) {
        this.starsFarSpeed  = far;
        this.starsMidSpeed  = mid;
        this.starsNearSpeed = near;
    }
    
    public void setEnemigoTexture(Texture tt) {
        this.enemigoTexture = tt;
    }

    @Override public void resize(int width, int height) {}
    @Override public void show() { lluvia.continuar(); }
    @Override public void hide() {}
    
    @Override
    public void pause() {
        lluvia.pausar();
        game.setScreen(new PausaScreen(game, this));
    }

    @Override public void resume() {}

    @Override
    public void dispose() {
        nave.destruir();
        lluvia.destruir();
        misilTexture.dispose();
        soldadoTexture.dispose();
        enemigoTexture.dispose();
        naveTexture.dispose();
        powerUpTexture.dispose();
        if (starTex != null) starTex.dispose();
        sonidoDisparo.dispose();
        hurtSound.dispose();
        sonidoRecarga.dispose();
        sonidoRescate.dispose();
        rainMusic.dispose();
        sonidoExplosion.dispose();
        vidaTexture.dispose();
    }
}