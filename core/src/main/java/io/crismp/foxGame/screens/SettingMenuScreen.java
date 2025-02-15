package io.crismp.foxGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;
import io.crismp.foxGame.tools.GamePreferences;

public class SettingMenuScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private Texture backgroundTexture;
    private BitmapFont font;
    private float musicVolume = 0.5f, soundVolume = 0.5f;
    private ImageButton btnMusica, btnSonidos, btnVibracion;
    private boolean isVibrationOn, isMusicOn, isSoundOn;
    private Label lblIdioma;
    private TextButton btnIdioma;
    private float lastMusicVolume, lastSoundVolume;
    Slider sliderMusica, sliderSonidos;

    public SettingMenuScreen(FoxGame game) {
        this.game = game;
        musicVolume = GamePreferences.getMusicVolume();
        soundVolume = GamePreferences.getSoundVolume();
        isVibrationOn = GamePreferences.isVibrationEnabled();
        isMusicOn = GamePreferences.getMusicVolume() > 0;
        isSoundOn = GamePreferences.getSoundVolume() > 0;
        lastMusicVolume = GamePreferences.getMusicVolume();
        lastSoundVolume = GamePreferences.getSoundVolume();
        stage = new Stage(new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera()));
        Gdx.input.setInputProcessor(stage);
        LanguageManager.loadLanguage();

        // Fondo del menú
        backgroundTexture = AssetsManager.getTexture("ui/background.png");

        // Fuente
        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(0.8f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Labels
        Label lblOpciones = new Label(LanguageManager.get("options").toUpperCase(), labelStyle);
        lblOpciones.setFontScale(1.25f);
        Label lblMusica = new Label(LanguageManager.get("music").toUpperCase(), labelStyle);
        Label lblSonidos = new Label(LanguageManager.get("sound").toUpperCase(), labelStyle);
        Label lblVibracion = new Label(LanguageManager.get("vibration").toUpperCase(), labelStyle);
        lblIdioma = new Label(LanguageManager.get("language").toUpperCase(), labelStyle);

        // Botón para cambiar de idioma
        btnIdioma = createTextButton(GamePreferences.getLanguage().equals("en") ? "Espanol" : "English",
                () -> toggleIdioma());
        btnIdioma.padBottom(8);

        // Botones de Música, Sonido y Vibración (Toggle)
        btnMusica = createToggleButton("ui/musica.png", "ui/musica_pulsado.png", () -> isMusicOn = !isMusicOn,
                isMusicOn);
        btnSonidos = createToggleButton("ui/sonidos.png", "ui/sonidos_pulsado.png",
                () -> isSoundOn = !isSoundOn,
                isSoundOn);
        btnVibracion = createToggleButton("ui/vibracion.png", "ui/vibracion_pulsado.png",
                () -> isVibrationOn = !isVibrationOn, isVibrationOn);

        // Sliders para controlar el volumen
        sliderMusica = createSlider(musicVolume, true);
        sliderSonidos = createSlider(soundVolume, false);

        // Botón de regreso al menú principal
        ImageButton btnMainMenu = createIconButton("ui/mainMenu.png", "ui/mainMenu_pulsado.png",
                () -> game.setScreen(new MainMenuScreen(game)));

        // Botones de Créditos y Ayuda
        TextButton btnCreditos = createTextButton(LanguageManager.get("credits").toUpperCase(),
                () -> mostrarPopUp(false));
        TextButton btnAyuda = createTextButton(LanguageManager.get("help").toUpperCase(),
                () -> mostrarPopUp(true));

        // Tabla para organizar los elementos
        Table table = new Table()
                .background(new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png")));
        table.setSize(FoxGame.V_WIDTH * 0.9f, FoxGame.V_HEIGHT * 1.9f);
        table.setPosition((FoxGame.V_WIDTH / 2) + 25, 10);
        table.add(lblOpciones).colspan(2).row();
        table.add(lblMusica).left();
        table.add(btnMusica).size(60, 60).row();
        table.add(sliderMusica).size(300, 5).colspan(2).padBottom(10).row();
        table.add(lblSonidos).left();
        table.add(btnSonidos).size(60, 60).row();
        table.add(sliderSonidos).size(300, 5).colspan(2).padBottom(10).row();
        table.add(lblVibracion).right();
        table.add(btnVibracion).size(60, 60).row();
        table.add(lblIdioma).right().padBottom(20);
        table.add(btnIdioma).size(120, 30).padLeft(10).padBottom(20).row();
        table.add(btnCreditos).size(120, 30).padRight(10);
        table.add(btnAyuda).size(120, 30).padLeft(10);

        // Agregar tablas al stage
        stage.addActor(table);
        stage.addActor(btnMainMenu);
    }

    // Método para crear botones con estado ON/OFF
    private ImageButton createToggleButton(String normalPath, String pressedPath, Runnable toggleAction,
            boolean isChecked) {

        TextureRegionDrawable normal = new TextureRegionDrawable(AssetsManager.getTexture(normalPath));
        TextureRegionDrawable pressed = new TextureRegionDrawable(AssetsManager.getTexture(pressedPath));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = isChecked ? normal : pressed;
        style.imageDown = pressed;

        ImageButton button = new ImageButton(style);
        button.setChecked(isChecked);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound3);
                toggleAction.run();
                boolean newState = button.isChecked();
                button.getStyle().imageUp = newState ? normal : pressed;

                // Guardar en preferencias y actualizar slider en funcion de lo marcado
                if (normalPath.equals("ui/musica.png")) {
                    if (GamePreferences.getMusicVolume() > 0) {
                        lastMusicVolume = GamePreferences.getMusicVolume();
                        GamePreferences.setMusicVolume(0);
                    } else {
                        GamePreferences.setMusicVolume(lastMusicVolume > 0 ? lastMusicVolume : 0.5f);
                    }
                    sliderMusica.setValue(GamePreferences.getMusicVolume());
                } else if (normalPath.equals("ui/sonidos.png")) {
                    if (GamePreferences.getSoundVolume() > 0) {
                        lastSoundVolume = GamePreferences.getSoundVolume();
                        GamePreferences.setSoundVolume(0);
                    } else {
                        GamePreferences.setSoundVolume(lastSoundVolume > 0 ? lastSoundVolume : 0.5f);
                    }
                    sliderSonidos.setValue(GamePreferences.getSoundVolume());
                } else if (normalPath.equals("ui/vibracion.png")) {
                    GamePreferences.setVibration(isVibrationOn);
                }
            }
        });

        return button;
    }

    // Método para crear botones de texto
    private TextButton createTextButton(String text, Runnable action) {
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle();
        estilo.font = font;
        estilo.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton.png"));
        estilo.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton_pulsado.png"));
        TextButton button = new TextButton(text, estilo);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                action.run();
            }
        });
        return button;
    }

    // Método para crear iconos con acción
    private ImageButton createIconButton(String normalPath, String pressedPath, Runnable action) {
        TextureRegionDrawable iconoButton = new TextureRegionDrawable(AssetsManager.getTexture(normalPath));
        TextureRegionDrawable iconoButtonMainMenuPulsado = new TextureRegionDrawable(
                AssetsManager.getTexture(pressedPath));
        ImageButton.ImageButtonStyle estilo = new ImageButton.ImageButtonStyle();
        estilo.imageUp = iconoButton;
        estilo.imageDown = iconoButtonMainMenuPulsado;
        ImageButton button = new ImageButton(estilo);
        button.setPosition((FoxGame.V_WIDTH * 2) - 20, (FoxGame.V_HEIGHT * 2) - 20, Align.topRight);
        button.setTransform(true);
        button.setScale(1.5f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playSound(game.clickSound);
                action.run();
            }
        });
        return button;
    }

    // Método para crear Sliders
    private Slider createSlider(float initialValue, boolean isMusic) {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/slide.png"));
        sliderStyle.knob = new TextureRegionDrawable(AssetsManager.getTexture("ui/slide_b.png"));

        Slider slider = new Slider(0, 1, 0.1f, false, sliderStyle);
        slider.setValue(initialValue);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playSound(game.clickSound2);
                float value = slider.getValue();

                if (isMusic) {
                    musicVolume = value;
                    GamePreferences.setMusicVolume(value);
                    game.updateMusicVolume();
                } else {
                    soundVolume = value;
                    GamePreferences.setSoundVolume(value);
                }
            }
        });

        return slider;
    }

    // Método para cambiar idioma
    private void toggleIdioma() {
        game.playSound(game.clickSound3);

        String nuevoIdioma = GamePreferences.getLanguage().equals("es") ? "en" : "es";
        GamePreferences.setLanguage(nuevoIdioma);
        LanguageManager.setLanguage(nuevoIdioma);
        LanguageManager.loadLanguage();

        // Recargar la pantalla con el nuevo idioma
        game.setScreen(new SettingMenuScreen(game));
    }

    // Método para mostrar un pop-up con una imagen
    private void mostrarPopUp(boolean help) {
        Skin skin = new Skin();
        skin.add("default-font", font);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton.png"));
        buttonStyle.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/boton_pulsado.png"));
        skin.add("default", buttonStyle);

        TextButton btnSalir = new TextButton(LanguageManager.get("exit").toUpperCase(), buttonStyle);
        btnSalir.getLabel().setFontScale(0.8f);
        btnSalir.setSize(100, 30);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png"));
        skin.add("default", windowStyle);

        Dialog dialog = new Dialog("", skin);

        // 🟢 Crear la tabla para el contenido
        Table table = new Table();

        if (help) {
            // *** TABLA DE AYUDA ***
            table.add(new Label(LanguageManager.get("help_1"), new Label.LabelStyle(font, Color.CYAN))).colspan(2);
            table.row();
            table.add(new Image(new Texture("ui/help/casa.png"))).colspan(2); // Imagen de Foxy
            table.row();
            table.add(new Label(LanguageManager.get("help1_2"), new Label.LabelStyle(font, Color.WHITE))).colspan(2)
                    .padTop(10);
            table.row();

            table.add(new Label(LanguageManager.get("help_2"), new Label.LabelStyle(font, Color.GREEN))).colspan(2)
                    .padTop(10);
            table.row();
            table.add(new Image(new Texture("ui/help/cherry.png"))).size(40, 40);
            table.add(new Label(LanguageManager.get("help2_1"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Image(new Texture("ui/help/gema.png"))).size(40, 40);
            table.add(new Label(LanguageManager.get("help2_2"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();

            table.add(new Label(LanguageManager.get("help_3"), new Label.LabelStyle(font, Color.RED))).colspan(2)
                    .padTop(10).padBottom(10);
            table.row();
            table.add(new Image(new Texture("ui/help/matar.png"))).size(50, 60).padRight(-20);
            Label enemigos = new Label(LanguageManager.get("help3_1"), new Label.LabelStyle(font, Color.WHITE));
            enemigos.setWrap(true);
            table.add(enemigos).width(200).padLeft(20);
            table.row();

            table.add(new Label(LanguageManager.get("help_4"), new Label.LabelStyle(font, Color.ORANGE))).colspan(2)
                    .padTop(10).padBottom(10);
            table.row();
            table.add(new Image(new Texture("ui/help/pinchos.png"))).size(50, 50).padRight(-20);
            Label pinchos = new Label(LanguageManager.get("help4_1"), new Label.LabelStyle(font, Color.WHITE));
            pinchos.setWrap(true);
            table.add(pinchos).width(200).padLeft(20);
            table.row();

        } else {
            // *** TABLA DE CREDITOS ***
            table.add(new Label(LanguageManager.get("credits").toUpperCase(), new Label.LabelStyle(font, Color.BLUE)));
            table.row();
            table.add(new Label(LanguageManager.get("code").toUpperCase(), new Label.LabelStyle(font, Color.GREEN)))
                    .padTop(20).padBottom(20);
            table.row();
            table.add(new Label(LanguageManager.get("autor"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("art").toUpperCase(), new Label.LabelStyle(font, Color.RED)))
                    .padTop(20).padBottom(20);
            table.row();
            table.add(new Label(LanguageManager.get("art_1"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("art_2"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("art_3"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("music_c").toUpperCase(), new Label.LabelStyle(font, Color.RED)))
                    .padTop(20).padBottom(20);
            table.row();
            table.add(new Label(LanguageManager.get("music_1"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("music_2"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("sound_c").toUpperCase(), new Label.LabelStyle(font, Color.RED)))
                    .padTop(20).padBottom(20);
            table.row();
            table.add(new Label(LanguageManager.get("sound_1"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("sound_2"), new Label.LabelStyle(font, Color.WHITE)));
            table.row();
            table.add(new Label(LanguageManager.get("acknowledgment").toUpperCase(),
                    new Label.LabelStyle(font, Color.GREEN)))
                    .padTop(20).padBottom(20);
            table.row();
            Label cod = new Label(LanguageManager.get("ack_1"), new Label.LabelStyle(font, Color.WHITE));
            cod.setWrap(true);
            cod.setAlignment(Align.center);
            table.add(cod).width(250).padLeft(20).padBottom(20);
            table.row();
            cod = new Label(LanguageManager.get("ack_2"), new Label.LabelStyle(font, Color.WHITE));
            cod.setWrap(true);
            cod.setAlignment(Align.center);
            table.add(cod).width(250).padLeft(20).padBottom(20);
            table.row();
            cod = new Label(LanguageManager.get("ack_3"), new Label.LabelStyle(font, Color.WHITE));
            cod.setWrap(true);
            cod.setAlignment(Align.center);
            table.add(cod).width(250).padLeft(20).padBottom(20);
            table.row();
            cod = new Label(LanguageManager.get("ack_4"), new Label.LabelStyle(font, Color.WHITE));
            cod.setWrap(true);
            cod.setAlignment(Align.center);
            table.add(cod).width(250).padLeft(20);
            
        }

        // 🟢 Agregar la tabla a un ScrollPane
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false);

        dialog.getContentTable().add(scrollPane).size(350, 300).padTop(20);
        dialog.button(btnSalir).padBottom(20);
        dialog.show(stage);
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
        game.playMusic("audio/music/joyful.ogg", true);

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
