package io.crismp.foxGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class MainMenuScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Texture titleTexture;
    private BitmapFont font;
    private TextureRegionDrawable btnNormal, btnPressed;
    private TextureRegionDrawable iconoOpciones, iconoRecords, iconoOpcionesPulsado, iconoRecordsPulsado;

    public MainMenuScreen(FoxGame game) {
        this.game = game;
        viewport = new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        
        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(1.15f);
        
        backgroundTexture = AssetsManager.getTexture("ui/background.png");
        Image background = new Image(new TextureRegionDrawable(backgroundTexture));
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        background.setPosition(0, 0);
        stage.addActor(background);
        titleTexture = AssetsManager.getTexture("ui/title.png");
        Image title = new Image(new TextureRegionDrawable(titleTexture));
        stage.addActor(title);
        title.setPosition(FoxGame.V_WIDTH / 4, FoxGame.V_HEIGHT * 1.25f);
        title.setSize(FoxGame.V_WIDTH * 1.5f,FoxGame.V_HEIGHT / 2);
       

        btnNormal = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton.png"));
        btnPressed = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton_pulsado.png"));
        iconoOpciones = new TextureRegionDrawable(AssetsManager.getTexture("ui/opciones.png"));
        iconoOpcionesPulsado = new TextureRegionDrawable(AssetsManager.getTexture("ui/opciones_pulsado.png"));
        iconoRecords = new TextureRegionDrawable(AssetsManager.getTexture("ui/records.png"));
        iconoRecordsPulsado = new TextureRegionDrawable(AssetsManager.getTexture("ui/records_pulsado.png"));

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = font;
        estiloBoton.up = btnNormal;
        estiloBoton.down = btnPressed;

        TextButton btnJugar = new TextButton(LanguageManager.get("play").toUpperCase(), estiloBoton);
        TextButton btnTutorial = new TextButton(LanguageManager.get("tutorial").toUpperCase(), estiloBoton);
        TextButton btnSalir = new TextButton(LanguageManager.get("exit").toUpperCase(), estiloBoton);
        btnJugar.padBottom(8);
        btnTutorial.padBottom(8);
        btnSalir.padBottom(8);

        ImageButton.ImageButtonStyle estiloOpciones = new ImageButton.ImageButtonStyle();
        estiloOpciones.imageUp = iconoOpciones;
        estiloOpciones.imageDown = iconoOpcionesPulsado;

        ImageButton.ImageButtonStyle estiloRecords = new ImageButton.ImageButtonStyle();
        estiloRecords.imageUp = iconoRecords;
        estiloRecords.imageDown = iconoRecordsPulsado;

        ImageButton btnOpciones = new ImageButton(estiloOpciones);
        ImageButton btnRecords = new ImageButton(estiloRecords);

        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(150);
        table.add(btnJugar).pad(10).row();
        table.add(btnTutorial).pad(10).row();
        table.add(btnSalir).pad(10).row();
        stage.addActor(table);

        btnOpciones.setPosition((FoxGame.V_WIDTH * 2) - 20, (FoxGame.V_HEIGHT * 2) - 20, Align.topRight);
        btnOpciones.setTransform(true);
        btnOpciones.setScale(1.5f);
        stage.addActor(btnOpciones);
        btnRecords.setPosition((FoxGame.V_WIDTH * 2) - 20, 20, Align.bottomRight);
        btnRecords.setTransform(true);
        btnRecords.setScale(1.5f);
        stage.addActor(btnRecords);

        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new PlayScreen(game, 1));
            }
        });

        btnTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new PlayScreen(game, 2));
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
                showRecordsDialog();
            }
        });
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        titleTexture.dispose();
        font.dispose();
        stage.dispose();
    }

    private void showRecordsDialog() {

        int[] scores = GamePreferences.getHighScores();
        Skin skin = new Skin();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton.png"));
        buttonStyle.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton_pulsado.png"));
        skin.add("default", buttonStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png"));
        skin.add("default", windowStyle);

        Dialog dialog = new Dialog("", skin);
        Table contentTable = new Table();

        for (int i = 0; i < scores.length; i++) {
            contentTable.add(new Label((i + 1) + ": " + scores[i] + " "+LanguageManager.get("points"), labelStyle)).row();
        }

        dialog.getContentTable().add(contentTable);
        dialog.button(LanguageManager.get("exit"));
        dialog.show(stage);
    }

}
