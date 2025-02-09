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
        btnIdioma = createTextButton(GamePreferences.getLanguage().equals("en")? "Espanol" : "English", () -> toggleIdioma());
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
        TextButton btnCreditos = createTextButton(LanguageManager.get("credits").toUpperCase(), () -> mostrarPopUp("ui/boton.png"));
        TextButton btnAyuda = createTextButton(LanguageManager.get("help").toUpperCase(), () -> mostrarPopUp("ui/boton.png"));

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
        System.out.println("Estaba en:"+GamePreferences.getLanguage());
        String nuevoIdioma = GamePreferences.getLanguage().equals("es") ? "en" : "es";
        GamePreferences.setLanguage(nuevoIdioma);
        LanguageManager.setLanguage(nuevoIdioma);
        System.out.println("Idioma cambiado a: " + nuevoIdioma);
        LanguageManager.loadLanguage();
        System.out.println("Idioma cargado: " + LanguageManager.get("options"));
        // Recargar la pantalla con el nuevo idioma
        game.setScreen(new SettingMenuScreen(game));
    }

    // Método para mostrar un pop-up con una imagen
    private void mostrarPopUp(String imagePath) {
        Dialog dialog = new Dialog("Hola", new Window.WindowStyle(font, Color.BLACK,
                new TextureRegionDrawable(AssetsManager.getTexture("ui/boton_cua.png"))));
        dialog.getContentTable().add(new Image(new Texture(imagePath)));
        dialog.button("Cerrar");
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
