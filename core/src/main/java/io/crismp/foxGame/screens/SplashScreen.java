package io.crismp.foxGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;

/**
 * Pantalla de bienvenida del juego (Splash Screen).
 * Muestra una imagen de fondo y un mensaje de "Presiona para continuar",
 * con un efecto de parpadeo. Al tocar la pantalla, se avanza al menú principal.
 */
public class SplashScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Texture backgroundTexture, pressTexture;
    private Image pressImage;
    public Music music;
    private float parpadeo = 0;

    /**
     * Constructor de la pantalla de bienvenida.
     * 
     * @param game Instancia principal del juego.
     */
    public SplashScreen(FoxGame game) {
        this.game = game;
        // Configurar el escenario y el viewport
        stage = new Stage(new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2), game.batch);
        Gdx.input.setInputProcessor(stage);

        // Cargar imágenes desde el AssetManager
        backgroundTexture = AssetsManager.getTexture("ui/splash/splashBack.png"); // Imagen de fondo
        pressTexture = AssetsManager.getTexture(LanguageManager.get("pulse")); // Imagen de "Presiona para continuar"

        // Crear la imagen de "Presiona para continuar"
        pressImage = new Image(pressTexture);
        pressImage.setScale(0.5f);
        pressImage.setPosition((stage.getWidth() - (pressImage.getWidth() / 2)) / 2, 50);
        stage.addActor(pressImage);

        // Reproducir música de fondo en bucle
        game.playMusic("audio/music/joyful.ogg", true);
    }

    @Override
    public void render(float delta) {
        parpadeo += delta;

        // Alternar visibilidad de la imagen cada 0.5 segundos (efecto de parpadeo)
        pressImage.setVisible(((int) (parpadeo * 2)) % 2 == 0);

        // Si el jugador toca la pantalla, cambiar a la pantalla de menú principal
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Limpiar la pantalla con un color negro
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar la imagen de fondo
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2);
        game.batch.end();

        // Actualizar y dibujar la escena
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Liberar memoria de las texturas
     */
    @Override
    public void dispose() {
        backgroundTexture.dispose();
        pressTexture.dispose();
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
