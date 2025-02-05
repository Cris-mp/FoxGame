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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.crismp.foxGame.FoxGame;

public class SettingMenuScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Texture backgroundTexture;
    private BitmapFont font;
    private boolean isMusicOn = true, isSoundOn = true;
    private ImageButton btnMusica, btnSonidos, btnVibracion;
    private SelectBox<String> selectIdioma;

    TextureRegionDrawable iconoMainMenu,iconoMainMenuPulsado;

    public SettingMenuScreen(FoxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera()));
        Gdx.input.setInputProcessor(stage);

        // Fondo del menú
        backgroundTexture = new Texture("ui/background.png");

        // Fuente
        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(0.8f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Labels
        Label lblOpciones = new Label("AJUSTES", labelStyle);
        lblOpciones.setFontScale(1.25f);
        Label lblMusica = new Label("MUSICA", labelStyle);
        Label lblSonidos = new Label("SONIDOS", labelStyle);
        Label lblVibracion = new Label("VIBRACION", labelStyle);
        iconoMainMenu = new TextureRegionDrawable(new Texture("ui/mainMenu.png"));
        iconoMainMenuPulsado = new TextureRegionDrawable(new Texture("ui/mainMenu_pulsado.png"));
        ImageButton.ImageButtonStyle estiloMainMenu = new ImageButton.ImageButtonStyle();
        estiloMainMenu.imageUp = iconoMainMenu;
        estiloMainMenu.imageDown = iconoMainMenuPulsado;
        ImageButton btnMainMenu = new ImageButton(estiloMainMenu);
        btnMainMenu.setPosition((FoxGame.V_WIDTH * 2) - 20, (FoxGame.V_HEIGHT * 2) - 20, Align.topRight);
        btnMainMenu.setTransform(true);
        btnMainMenu.setScale(1.5f);
        stage.addActor(btnMainMenu);

        // Botones de Música, Sonido y Vibración (Toggle)
        btnMusica = createToggleButton("ui/musica.png", "ui/musica_pulsado.png", () -> isMusicOn = !isMusicOn,false);//TODO:poner la booleana correcta;
        btnSonidos = createToggleButton("ui/sonidos.png", "ui/sonidos_pulsado.png", () -> isSoundOn = !isSoundOn,false);
        btnVibracion = createToggleButton("ui/boton_p.png", "ui/boton_p_pulsado.png",
                () -> game.isVibrationOn = !game.isVibrationOn,game.isVibrationOn);

        // Cargar texturas para el fondo y la lista desplegable
        TextureRegionDrawable background = new TextureRegionDrawable(new Texture("ui/boton_cua.png"));
        TextureRegionDrawable listBackground = new TextureRegionDrawable(new Texture("ui/boton_cua_pulsado.png"));
        // Estilo del selectedbox
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.background = background;
        selectBoxStyle.listStyle = new List.ListStyle();
        selectBoxStyle.listStyle.font = font;
        selectBoxStyle.listStyle.background = listBackground;
        selectBoxStyle.listStyle.selection = new TextureRegionDrawable(new Texture("ui/boton_cua.png"));
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        // SelectBox para el idioma
        selectIdioma = new SelectBox<>(selectBoxStyle);
        selectIdioma.setAlignment(Align.center); // Centrar el texto
        selectIdioma.getList().setAlignment(Align.center); // Centrar la lista de opciones

        selectIdioma.setItems("Espanol", "Ingles");
        selectIdioma.setSelected("Español");

        // Botones de Créditos y Ayuda (con texto)
        TextButton btnCreditos = createTextButton("CREDITOS");
        TextButton btnAyuda = createTextButton("AYUDA");
        btnCreditos.padBottom(8);
        btnAyuda.padBottom(8);

        // Tabla para organizar los elementos
        Table table = new Table().background(new TextureRegionDrawable(new Texture("ui/backSettings.png")));
        table.setSize(FoxGame.V_WIDTH * 0.9f, FoxGame.V_HEIGHT * 1.7f);
        table.setPosition((FoxGame.V_WIDTH / 2)+25, 30);
        table.add(lblOpciones).colspan(2).row();
        table.add(lblMusica).right();
        table.add(btnMusica).size(60, 60).row();
        table.add(lblSonidos).right().padTop(-20);
        table.add(btnSonidos).size(60, 60).padTop(-20).row();
        table.add(lblVibracion).right().padTop(-20);
        table.add(btnVibracion).size(60, 60).padTop(-20).row();

        table.add(selectIdioma).size(120, 40).padBottom(20).colspan(2).row();
        table.add(btnCreditos).size(130, 40).padRight(10);
        table.add(btnAyuda).size(130, 40).padLeft(10);

        // Agregar tablas al stage
        stage.addActor(table);

        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        btnVibracion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });


    }

    // Método para crear botones con estado ON/OFF
    private ImageButton createToggleButton(String normalPath, String pressedPath, Runnable toggleAction, boolean isChecked) {
        Texture normalTexture = new Texture(normalPath);
        Texture pressedTexture = new Texture(pressedPath);
        TextureRegionDrawable normal = new TextureRegionDrawable(normalTexture);
        TextureRegionDrawable pressed = new TextureRegionDrawable(pressedTexture);
        ImageButton button = new ImageButton(normal, pressed);
        button.setChecked(isChecked);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleAction.run();
                // Cambia la imagen manualmente en función del estado
                System.out.println(game.isVibrationOn);
                if (button.isChecked()) {
                    button.getStyle().imageUp = pressed;
                } else {
                    button.getStyle().imageUp = normal;
                }
            }
        });
        return button;
    }

    // Método para botones de texto
    private TextButton createTextButton(String text) {
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle();
        estilo.font = font;
        estilo.up = new TextureRegionDrawable(new Texture("ui/boton.png"));
        estilo.down = new TextureRegionDrawable(new Texture("ui/boton_pulsado.png"));
        return new TextButton(text, estilo);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2);
        game.batch.end();
        stage.act(delta);
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
        backgroundTexture.dispose();
    }

    // Métodos vacíos de la interfaz Screen
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
