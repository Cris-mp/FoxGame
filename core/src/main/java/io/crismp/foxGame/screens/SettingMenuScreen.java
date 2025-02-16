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

/**
 * Pantalla de configuración del juego, donde el usuario puede ajustar el
 * volumen de música y efectos, activar o desactivar la vibración y cambiar el
 * idioma.
 */
public class SettingMenuScreen implements Screen {
    private FoxGame game;
    private Stage stage;
    private BitmapFont font;
    private float musicVolume = 0.5f, soundVolume = 0.5f;
    private ImageButton btnMusica, btnSonidos, btnVibracion;
    private boolean isVibrationOn, isMusicOn, isSoundOn;
    private TextButton btnIdioma;
    private float lastMusicVolume, lastSoundVolume;
    private Slider sliderMusica, sliderSonidos;

    /**
     * Constructor de la pantalla de configuración del juego.
     * <p>
     * Este constructor inicializa los valores de configuración del juego
     * relacionados con el volumen de música, volumen de sonido y vibración,
     * así como los estados de los controles de música y sonido. También establece
     * la vista del escenario utilizando un viewport ajustado a las dimensiones de
     * la pantalla del juego, y carga el idioma actual utilizando el
     * LanguageManager.
     * Además, se configura la entrada del usuario para que se procese mediante el
     * escenario actual. Finalmente, se agrega una imagen de fondo a la pantalla
     * y se inicializan los elementos de la interfaz de usuario (UI).
     * </p>
     *
     * @param game El objeto de la clase FoxGame que representa el juego actual.
     *             Se utiliza para acceder a las preferencias y otros recursos del
     *             juego.
     */
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

        // Crear y añadir imagen de fondo
        Image background = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/background.png")));
        background.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(background);

        // Fuente
        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(0.8f);

        inicializarUI();
    }

    /**
     * Inicializa la interfaz de usuario (UI) del menú de configuración.
     * <p>
     * Esta función crea y configura todos los elementos visuales del menú de
     * opciones, como etiquetas de texto, botones de alternancia (toggle buttons)
     * para música, sonido y vibración, sliders para ajustar los volúmenes,
     * un botón para cambiar el idioma, y botones para acceder a los créditos y la
     * ayuda. Los elementos se organizan en una tabla que se añade al escenario de
     * la
     * aplicación. También se incluyen efectos visuales como el cambio de
     * imágenes en los botones y la actualización de los valores de configuración
     * cuando se interactúa con los controles.
     * </p>
     */
    private void inicializarUI() {
        // Crear etiquetas de texto
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label lblOpciones = new Label(LanguageManager.get("options").toUpperCase(), labelStyle);
        lblOpciones.setFontScale(1.25f);
        Label lblMusica = new Label(LanguageManager.get("music").toUpperCase(), labelStyle);
        Label lblSonidos = new Label(LanguageManager.get("sound").toUpperCase(), labelStyle);
        Label lblVibracion = new Label(LanguageManager.get("vibration").toUpperCase(), labelStyle);
        Label lblIdioma = new Label(LanguageManager.get("language").toUpperCase(), labelStyle);

        // Botones de Música, Sonido y Vibración (Toggle)
        btnMusica = createToggleButton("ui/button/tglMusic_on.png", "ui/button/tglMusic_off.png",
                () -> isMusicOn = !isMusicOn,
                isMusicOn);
        btnSonidos = createToggleButton("ui/button/tglSound_on.png", "ui/button/tglSound_off.png",
                () -> isSoundOn = !isSoundOn,
                isSoundOn);
        btnVibracion = createToggleButton("ui/button/tglVibration_on.png", "ui/button/tglVibration_off.png",
                () -> isVibrationOn = !isVibrationOn, isVibrationOn);

        // Sliders para controlar el volumen
        sliderMusica = createSlider(musicVolume, true);
        sliderSonidos = createSlider(soundVolume, false);

        // Botón para cambiar de idioma
        btnIdioma = createTextButton(GamePreferences.getLanguage().equals("en") ? "English" : "Espanol",
                () -> toggleIdioma());
        btnIdioma.padBottom(8);

        // Botón de regreso al menú principal
        ImageButton btnMainMenu = createIconButton("ui/button/btnMainMenu.png", "ui/button/btnMainMenu_p.png",
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

    /**
     * Crea un botón de tipo toggle (interruptor) con imágenes para los estados
     * normal y presionado.
     * <p>
     * Esta función crea un botón de tipo <code>ImageButton</code> que puede
     * alternar entre dos estados (activado o desactivado).
     * Dependiendo del estado <code>isChecked</code>, se muestra una imagen
     * diferente para el estado normal del botón.
     * Al hacer clic en el botón, se ejecuta una acción proporcionada como
     * parámetro, y el estado del botón (checked o unchecked) se alterna.
     * Además, se actualizan las preferencias del juego (volumen de
     * música, volumen de sonido o vibración) y se ajustan los valores de
     * los sliders correspondientes.
     * </p>
     *
     * @param normalPath   Ruta de la imagen que representa el estado normal del
     *                     botón (cuando está activado).
     * @param pressedPath  Ruta de la imagen que representa el estado presionado del
     *                     botón.
     * @param toggleAction Acción que se ejecuta cuando el usuario hace clic en el
     *                     botón para alternar el estado.
     * @param isChecked    Estado inicial del botón (true si está activado, false si
     *                     está desactivado).
     * @return Un <code>ImageButton</code> configurado con las imágenes, la acción y
     *         el estado inicial proporcionado.
     */
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
                button.getStyle().imageUp = button.isChecked() ? normal : pressed;

                // Guardar en preferencias y actualizar slider en funcion de lo marcado
                if (normalPath.equals("ui/button/tglMusic_on.png")) {
                    if (GamePreferences.getMusicVolume() > 0) {
                        lastMusicVolume = GamePreferences.getMusicVolume();
                        GamePreferences.setMusicVolume(0);
                    } else {
                        GamePreferences.setMusicVolume(lastMusicVolume > 0 ? lastMusicVolume : 0.5f);
                    }
                    sliderMusica.setValue(GamePreferences.getMusicVolume());
                } else if (normalPath.equals("ui/button/tglSound_on.png")) {
                    if (GamePreferences.getSoundVolume() > 0) {
                        lastSoundVolume = GamePreferences.getSoundVolume();
                        GamePreferences.setSoundVolume(0);
                    } else {
                        GamePreferences.setSoundVolume(lastSoundVolume > 0 ? lastSoundVolume : 0.5f);
                    }
                    sliderSonidos.setValue(GamePreferences.getSoundVolume());
                } else if (normalPath.equals("ui/button/tglVibration_on.png")) {
                    GamePreferences.setVibration(isVibrationOn);
                }
            }
        });
        return button;
    }

    /**
     * Crea un botón de texto con una acción asociada al hacer clic.
     * <p>
     * Esta función crea un botón de tipo <code>TextButton</code> con un texto
     * proporcionado como parámetro, y con un estilo personalizado que incluye
     * imágenes para los estados normal y presionado del botón.
     * Al hacer clic en el botón, se ejecuta la acción proporcionada y se reproduce
     * un sonido de clic.
     * </p>
     *
     * @param text   El texto que se mostrará en el botón.
     * @param action Acción que se ejecuta cuando el usuario hace clic en el botón.
     * @return Un <code>TextButton</code> configurado con el texto y la acción
     *         proporcionada.
     */
    private TextButton createTextButton(String text, Runnable action) {
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle();
        estilo.font = font;
        estilo.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval.png"));
        estilo.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval_p.png"));
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

    /**
     * Crea un botón de icono con una imagen normal y una imagen al ser presionado.
     * <p>
     * Esta función genera un botón de tipo <code>ImageButton</code> con una imagen
     * que cambia cuando el usuario lo presiona. Además, el botón se posiciona en la
     * esquina superior derecha de la pantalla y tiene un tamaño de escala
     * aumentado. Al hacer clic en el botón, se ejecuta una acción proporcionada
     * como parámetro y se reproduce un sonido de clic.
     * </p>
     *
     * @param normalPath  Ruta de la imagen que representa el estado normal del
     *                    botón.
     * @param pressedPath Ruta de la imagen que representa el estado presionado del
     *                    botón.
     * @param action      Acción que se ejecuta cuando el usuario hace clic en el
     *                    botón.
     * @return Un <code>ImageButton</code> configurado con las imágenes y la acción
     *         proporcionada.
     */
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

    /**
     * Crea un control deslizante (slider) para ajustar el volumen de música o
     * efectos de sonido.
     * <p>
     * Esta función crea un slider que permite al usuario ajustar el volumen de la
     * música o los efectos de sonido.Dependiendo del parámetro
     * <code>isMusic</code>, se ajusta el volumen de la música o de los sonidos.
     * Cuando el usuario interactúa con el slider, se actualiza el valor del
     * volumen y se ajusta el botón de mute en consecuencia. Además, se reproduce
     * un sonido de clic cada vez que el valor del slider cambia.
     * </p>
     *
     * @param initialValue Valor inicial del slider, entre 0 y 1, que representa el
     *                     volumen inicial.
     * @param isMusic      Booleano que indica si el slider controla el volumen de
     *                     la música (true) o de los efectos de sonido (false).
     * @return Un slider configurado para ajustar el volumen.
     */
    private Slider createSlider(float initialValue, boolean isMusic) {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/backSlide.png"));
        sliderStyle.knob = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnSlide.png"));

        Slider slider = new Slider(0, 1, 0.1f, false, sliderStyle);
        slider.setValue(initialValue);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = slider.getValue();
                game.playSound(game.clickSound2);

                if (isMusic) {
                    musicVolume = value;
                    GamePreferences.setMusicVolume(value);
                    game.updateMusicVolume();
                    actualizarBotonMute(btnMusica, value > 0);
                } else {
                    soundVolume = value;
                    GamePreferences.setSoundVolume(value);
                    actualizarBotonMute(btnSonidos, value > 0);
                }
            }
        });
        return slider;
    }

    /**
     * Actualiza la imagen del botón de mute (silenciar) según su estado (activo o
     * inactivo).
     * <p>
     * Dependiendo de si el botón está activo o no, se cambia la imagen mostrada en
     * el botón. Si el botón corresponde a la música (btnMusica), se usa una imagen
     * relacionada con la música, de lo contrario, se usa una imagen para los
     * efectos de sonido.
     * </p>
     * 
     * @param boton  El botón de imagen que se actualizará (puede ser el botón de
     *               música o de sonido).
     * @param activo Estado del botón (true si está activo, false si está inactivo).
     */
    private void actualizarBotonMute(ImageButton boton, boolean activo) {
        String path = activo ? "on" : "off";
        if (boton == btnMusica) {
            boton.getStyle().imageUp = new TextureRegionDrawable(
                    AssetsManager.getTexture("ui/button/tglMusic_" + path + ".png"));
        } else {
            boton.getStyle().imageUp = new TextureRegionDrawable(
                    AssetsManager.getTexture("ui/button/tglSound_" + path + ".png"));
        }
    }

    /**
     * Cambia el idioma de la aplicación entre español e inglés.
     * <p>
     * Esta función alterna el idioma actual, cambiando entre "es" (español) y "en"
     * (inglés).
     * Cuando se cambia el idioma, se actualizan las preferencias de idioma del
     * juego,se recarga el lenguaje a través del <code>LanguageManager</code> y se
     * recarga la pantalla con el nuevo idioma aplicado.
     * </p>
     * Además, se reproduce un sonido de clic cada vez que se cambia el idioma.
     */
    private void toggleIdioma() {
        game.playSound(game.clickSound3);

        String nuevoIdioma = GamePreferences.getLanguage().equals("es") ? "en" : "es";
        GamePreferences.setLanguage(nuevoIdioma);
        LanguageManager.setLanguage(nuevoIdioma);
        LanguageManager.loadLanguage();

        // Recargar la pantalla con el nuevo idioma
        game.setScreen(new SettingMenuScreen(game));
    }

    /**
     * Muestra un cuadro de diálogo (popup) con información de ayuda o créditos,
     * según el parámetro especificado.
     * <p>
     * Si el parámetro <code>help</code> es verdadero, se mostrará una serie de
     * instrucciones de ayuda con imágenes y texto. Si es falso, se mostrarán
     * los créditos del juego, incluyendo el código, el arte, la música y los
     * efectos de sonido.
     * </p>
     * El contenido se organiza en una tabla con diferentes estilos y se presenta en
     * un <code>Dialog</code> en la interfaz de usuario. Se proporciona un botón de
     * salida al final del cuadro de diálogo.
     * 
     * @param help booleano que determina el contenido del cuadro de diálogo:
     *             - <code>true</code> para mostrar la ayuda.
     *             - <code>false</code> para mostrar los créditos.
     */
    private void mostrarPopUp(boolean help) {
        Skin skin = new Skin();
        skin.add("default-font", font);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval.png"));
        buttonStyle.down = new TextureRegionDrawable(AssetsManager.getTexture("ui/button/btnOval_p.png"));
        skin.add("default", buttonStyle);

        TextButton btnSalir = new TextButton(LanguageManager.get("exit").toUpperCase(), buttonStyle);
        btnSalir.getLabel().setFontScale(0.8f);
        btnSalir.setSize(100, 30);

        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.background = new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png"));
        skin.add("default", windowStyle);

        Dialog dialog = new Dialog("", skin);

        // Crear la tabla para el contenido
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

        // Agregar la tabla a un ScrollPane
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false);

        dialog.getContentTable().add(scrollPane).size(350, 300).padTop(20);
        dialog.button(btnSalir).padBottom(20);
        dialog.show(stage);
    }

    /**
     * Método encargado de renderizar la pantalla en cada cuadro.
     * <p>
     * Este método es llamado por el motor del juego en cada fotograma para
     * actualizar y dibujar la pantalla. Se encarga de limpiar el buffer de
     * pantalla, estableciendo un color de fondo negro, luego actualiza los
     * actores en el escenario (stage) y finalmente dibuja todos los elementos
     * del escenario en pantalla.
     * </p>
     *
     * @param delta El tiempo transcurrido (en segundos) desde el último fotograma.
     *              Este parámetro se utiliza para actualizar las animaciones y la
     *              lógica de la pantalla.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Método encargado de redimensionar el escenario cuando el tamaño de la ventana
     * cambia.
     * <p>
     * Este método se invoca cuando la ventana del juego es redimensionada.
     * Actualiza el tamaño del viewport>del escenario para adaptarse a las nuevas
     * dimensiones de la ventana, garantizando que los elementos
     * en la pantalla se ajusten correctamente al nuevo tamaño.
     * </p>
     *
     * @param width  El nuevo ancho de la ventana en píxeles.
     * @param height El nuevo alto de la ventana en píxeles.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Método encargado de liberar los recursos utilizados por la pantalla.
     * <p>
     * Este método se invoca cuando la pantalla ya no es necesaria y se está a punto
     * de destruirla. Se utiliza para liberar memoria y recursos como texturas y
     * fuentes que ya no serán usados,evitando así posibles fugas de memoria.
     * En este caso, se eliminan la fuente utilizada y el escenario.
     * </p>
     */
    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

    @Override
    public void show() {
        game.playMusic("audio/music/joyful.ogg", true);
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
