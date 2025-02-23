package io.crismp.foxGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;
import io.crismp.foxGame.tools.GamePreferences;

/**
 * Pantalla de Game Over.
 * Muestra un mensaje de "Game Over", una imagen de calavera y reproduce música
 * triste.
 * Al tocar la pantalla, el jugador regresa al menú principal.
 */
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font;
    private FoxGame game;
    private float elapsedTime = 0;

    /**
     * Constructor de la pantalla de Game Over.
     * 
     * @param game Instancia principal del juego.
     */
    public GameOverScreen(FoxGame game) {
        this.game = game;

        // Configurar la cámara y el viewport
        viewport = new FitViewport(FoxGame.V_WIDTH, FoxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        // Reproducir música de Game Over
        game.playMusic("audio/music/To Suffer a Loss (Game Over).ogg", false);

        // Configurar la fuente del texto
        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(1f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Crear un contenedor (Table) para organizar elementos
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        // Crear etiqueta de "Game Over" con soporte para múltiples idiomas
        Label gameOverLabel = new Label(LanguageManager.get("game_over"), labelStyle);

        // Cargar imagen de calavera desde el AssetManager
        Image skull = (new Image(new TextureRegion(AssetsManager.getTexture("hud/skull2.png"), 0, 0, 430, 414)));

        // Agregar elementos a la tabla y esta al escenario
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(skull).size(100, 100);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta; // Acumular tiempo

        // Permitir cambio de pantalla solo después de 1.5 segundos
        if (elapsedTime > 1.5f && Gdx.input.justTouched()) {
            GamePreferences.saveScore(0, true);
            GamePreferences.resetAccumulatedScore();
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar la escena
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

    // Métodos no utilizados, pero necesarios por la interfaz Screen
    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
