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


public class SplashScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Texture backgroundTexture, pressTexture;
    private Image pressImage;
    public  Music music;

    private float elapsedTime = 0; // Tiempo transcurrido para efecto de parpadeo

    public SplashScreen(FoxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2), game.batch);
        Gdx.input.setInputProcessor(stage);

        // Cargar imágenes
        backgroundTexture = AssetsManager.getTexture("ui/splash/splashBack.png"); // Imagen de fondo
        pressTexture = AssetsManager.getTexture(LanguageManager.get("pulse")); // Imagen de "Presiona para continuar"

        // Crear imagen de presionar pantalla
        pressImage = new Image(pressTexture);
        pressImage.setScale(0.5f);
        pressImage.setPosition((stage.getWidth() - (pressImage.getWidth() / 2)) / 2, 50);
        stage.addActor(pressImage);
        game.playMusic("audio/music/joyful.ogg",true);
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        // Parpadeo del texto cada 0.5 segundos
        pressImage.setVisible(((int) (elapsedTime * 2)) % 2 == 0);

        // Detectar toque o clic para cambiar a MainMenuScreen
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Dibujar fondo y elementos
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        pressTexture.dispose();
        stage.dispose();
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
