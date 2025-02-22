package io.crismp.foxGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;
import io.crismp.foxGame.tools.GamePreferences;

/**
 * Clase que representa la pantalla del menú principal del juego.
 * Contiene los botones para iniciar el juego, acceder al tutorial, ver récords,
 * abrir las opciones y salir del juego.
 */
public class MainMenuScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;
    private Skin skin;

    /**
     * Constructor de la pantalla del menú principal.
     *
     * @param game Referencia a la instancia principal del juego
     */
    public MainMenuScreen(FoxGame game) {
        this.game = game;

        // Configurar la vista con un FitViewport para mantener la relación de aspecto
        viewport = new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        // Asignar el escenario como procesador de entrada
        Gdx.input.setInputProcessor(stage);

        // Cargar la fuente y ajustar su tamaño
        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(1.15f);

        // Crear y añadir imagen de fondo
        Image background = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/background.png")));
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(background);

        // Crear y añadir imagen de título
        Image title = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/title.png")));
        title.setSize(FoxGame.V_WIDTH * 1.5f, FoxGame.V_HEIGHT / 2);

        // Estilo para los botones
        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = font;
        estiloBoton.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval.png"));
        estiloBoton.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval_p.png"));

        // Creación de botones
        TextButton btnJugar = new TextButton(LanguageManager.get("play").toUpperCase(), estiloBoton);
        TextButton btnTutorial = new TextButton(LanguageManager.get("tutorial").toUpperCase(), estiloBoton);
        TextButton btnSalir = new TextButton(LanguageManager.get("exit").toUpperCase(), estiloBoton);
        btnJugar.padBottom(8);
        btnTutorial.padBottom(8);
        btnSalir.padBottom(8);

        // Botón de opciones
        ImageButton btnOpciones = new ImageButton(
                new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOptions.png")),
                new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOptions_p.png")));

        // Botón de récords
        ImageButton btnRecords = new ImageButton(
                new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnRecords.png")),
                new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnRecords_p.png")));

        // Crear tabla para organizar los elementos
        Table table = new Table();
        table.setFillParent(true);
        table.add(title).pad(45).padBottom(10).row();
        table.add(btnJugar).pad(10).row();
        table.add(btnTutorial).pad(10).row();
        table.add(btnSalir).pad(10).padBottom(20).row();
        stage.addActor(table);

        // Posicionamiento de botones adicionales
        btnOpciones.setPosition((FoxGame.V_WIDTH * 2) - 20, (FoxGame.V_HEIGHT * 2) - 20, Align.topRight);
        btnOpciones.setTransform(true);
        btnOpciones.setScale(1.5f);
        btnRecords.setPosition((FoxGame.V_WIDTH * 2) - 20, 20, Align.bottomRight);
        btnRecords.setTransform(true);
        btnRecords.setScale(1.5f);
        stage.addActor(btnOpciones);
        stage.addActor(btnRecords);

        // Asignar acciones a los botones
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new PlayScreen(game, 1));
                //game.setScreen(new PlayScreen(game, 2));
            }
        });

        btnTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new PlayScreen(game, 0));
            }
        });

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                System.exit(0);
                Gdx.app.exit();
            }
        });

        btnOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new SettingMenuScreen(game));
            }
        });

        btnRecords.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                showRecordsDialog();
            }
        });
        skin = new Skin();
    }

    /**
     * Muestra un diálogo con los récords del juego.
     */
    private void showRecordsDialog() {

        int[] scores = GamePreferences.getHighScores();
        Skin skin = new Skin();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval.png"));
        buttonStyle.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval_p.png"));
        skin.add("default", buttonStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png"));
        skin.add("default", windowStyle);

        Dialog dialog = new Dialog("", skin);
        Table contentTable = new Table();

        if (scores.length == 0) {
            contentTable.add(new Label(LanguageManager.get("no_scores").toUpperCase(), labelStyle)).row();
        } else {
            contentTable.add(new Label(LanguageManager.get("high_scores").toUpperCase(), labelStyle)).padBottom(10).row();
            for (int i = 0; i < scores.length; i++) {
                contentTable
                        .add(new Label((i + 1) + ": " + scores[i] + " " + LanguageManager.get("points"), labelStyle))
                        .row();
            }
        }

        dialog.getContentTable().add(contentTable);
        dialog.button(LanguageManager.get("exit")).padBottom(40).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
            }
        });
        dialog.show(stage);
    }

    @Override
    public void show() {
        game.playMusic("audio/music/joyful.ogg", true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
        skin.dispose();
    }

    // Métodos no utilizados, pero necesarios por la interfaz Screen
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
