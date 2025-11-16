package com.mygdx.warnav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {

    private final GameLluviaMenu game;
    private SpriteBatch batch;	   
    private BitmapFont font;
    private OrthographicCamera camera;

    // === Datos de la partida ===
    private int puntosFinales;

    // === Ranking / Singleton ===
    private GestorRanking gestorRanking;
    private List<UserPuntaje> vecinos = new ArrayList<>();
    private UserPuntaje miRegistro;

    // === Nombre del jugador ===
    private String nombreActual = "";
    private boolean nombreConfirmado = false;

    private GlyphLayout layout = new GlyphLayout();

    public GameOverScreen(final GameLluviaMenu game, int puntosFinales) {
        this.game = game;
        this.puntosFinales = puntosFinales;

        this.batch = game.getBatch();
        this.font = game.getFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        gestorRanking = GestorRanking.getInstance();
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.05f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (!nombreConfirmado) {
            actualizarNombrePorTeclado();
        }

        batch.begin();

        // === TÍTULO ===
        font.getData().setScale(2.0f);
        font.setColor(Color.RED);
        drawCentered("GAME OVER", 420);

        // === PUNTOS FINALES ===
        font.getData().setScale(1.2f);
        font.setColor(Color.WHITE);
        drawCentered("Puntaje final: " + puntosFinales, 380);

        if (!nombreConfirmado) {
            // ===== FASE 1: INGRESO DE NOMBRE =====
            font.getData().setScale(1.0f);
            font.setColor(Color.YELLOW);
            drawCentered("Ingresa tu nombre y presiona ENTER", 320);

            font.setColor(Color.CYAN);
            String mostrar = nombreActual;
            if ((System.currentTimeMillis() / 500) % 2 == 0) {
                mostrar += "_";
            }
            drawCentered(mostrar, 290);

            font.getData().setScale(0.9f);
            font.setColor(Color.LIGHT_GRAY);
            drawCentered("(Máx. 10 caracteres | A-Z, 0-9 | BACKSPACE para borrar)", 260);

        } else {
            // ===== FASE 2: MOSTRAR VECINOS EN COLUMNA CENTRAL =====
            font.getData().setScale(1.0f);
            font.setColor(Color.ORANGE);
            drawCentered("Tu posición en el ranking", 350);

            // Encontrar el índice de tu registro dentro de vecinos
            int indexSelf = -1;
            for (int i = 0; i < vecinos.size(); i++) {
                UserPuntaje p = vecinos.get(i);
                if (p.getNombreJugador().equals(miRegistro.getNombreJugador())
                        && p.getPuntos() == miRegistro.getPuntos()) {
                    indexSelf = i;
                    break;
                }
            }

            float centerY = 260f;             // donde quedas tú
            float rowStep = 24f;              // separación vertical entre filas

            if (indexSelf == -1) {
                // Por si acaso, pero no debería pasar
                indexSelf = vecinos.size() / 2;
            }

            // Queremos que tu fila quede en centerY:
            // y = startY - i*rowStep  =>  para i=indexSelf  -> centerY
            // => startY = centerY + indexSelf*rowStep
            float startY = centerY + indexSelf * rowStep;

            for (int i = 0; i < vecinos.size(); i++) {
                UserPuntaje p = vecinos.get(i);
                float y = startY - i * rowStep;

                String linea = p.getNombreJugador() + " - " + p.getPuntos() + " pts";

                if (i == indexSelf) {
                    font.getData().setScale(1.1f);
                    font.setColor(Color.YELLOW);
                } else {
                    font.getData().setScale(0.9f);
                    font.setColor(Color.LIGHT_GRAY);
                }

                drawCentered(linea, y);
            }

            // Texto para volver a jugar
            font.getData().setScale(1.0f);
            font.setColor(Color.SKY);
            drawCentered("Click en cualquier parte para jugar de nuevo", 120);
        }

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);

        batch.end();

        if (nombreConfirmado && Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void drawCentered(String text, float y) {
        layout.setText(font, text);
        float x = (camera.viewportWidth - layout.width) / 2f;
        font.draw(batch, text, x, y);
    }

    private void actualizarNombrePorTeclado() {
        // Letras A-Z
        for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
            if (Gdx.input.isKeyJustPressed(key) && nombreActual.length() < 10) {
                char c = (char) ('A' + (key - Input.Keys.A));
                nombreActual += c;
            }
        }

        // Números 0-9
        for (int key = Input.Keys.NUM_0; key <= Input.Keys.NUM_9; key++) {
            if (Gdx.input.isKeyJustPressed(key) && nombreActual.length() < 10) {
                char c = (char) ('0' + (key - Input.Keys.NUM_0));
                nombreActual += c;
            }
        }

        // Borrar con BACKSPACE
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && nombreActual.length() > 0) {
            nombreActual = nombreActual.substring(0, nombreActual.length() - 1);
        }

        // Confirmar con ENTER
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && nombreActual.length() > 0) {
            miRegistro = new UserPuntaje(nombreActual, puntosFinales);

            // 1) Agregar al ranking global
            gestorRanking.agregarPuntaje(nombreActual, puntosFinales);

            // 2) Obtener vecinos (incluye al propio jugador)
            vecinos = gestorRanking.getVecinos(miRegistro);
            if (vecinos == null) {
                vecinos = new ArrayList<>();
            }

            nombreConfirmado = true;
        }
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}
