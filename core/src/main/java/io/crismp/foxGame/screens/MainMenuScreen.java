package io.crismp.foxGame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Tools.AssetsManager;

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
        game.playMusic("audio/music/joyful.ogg",true);
        viewport = new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(1.15f);

        backgroundTexture = AssetsManager.getTexture("ui/background.png");
        titleTexture = AssetsManager.getTexture("ui/title.png");

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

        TextButton btnJugar = new TextButton("JUGAR", estiloBoton);
        TextButton btnTutorial = new TextButton("TUTORIAL", estiloBoton);
        TextButton btnSalir = new TextButton("SALIR", estiloBoton);
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
                game.setScreen(new PlayScreen(game));
            }
        });

        btnTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                game.setScreen(new PlayScreen(game)); // Aquí puedes poner la pantalla de opciones si tienes una
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
                game.setScreen(new PlayScreen(game)); // Aquí puedes poner la pantalla de récords si tienes una
            }
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2);
        game.batch.draw(titleTexture, FoxGame.V_WIDTH / 4, FoxGame.V_HEIGHT * 1.25f, FoxGame.V_WIDTH * 1.5f,
                FoxGame.V_HEIGHT / 2);
        game.batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
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
}
