package io.crismp.foxGame.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;

/**
 * Clase que gestiona el HUD (Head-Up Display) del juego.
 * Muestra información como vidas, cerezas y gemas recogidas, además de carteles
 * de tutorial.
 */
public class Hud implements Disposable {
    // Elementos principales del HUD
    public Stage stage; // Escenario donde se renderizan los elementos del HUD
    private Viewport viewport; // Viewport para manejar la resolución del HUD

    // Fuentes para los textos del HUD
    private BitmapFont font;
    private BitmapFont littleFont;

    // Variables de estado del HUD
    private int cherries;
    private int gems;
    private Integer life;

    // Etiquetas y imagenes para mostrar las cerezas y gemas recogidas
    private Label lblCherries;
    private Label lblGems;
    private Image cherry;
    private Image gem;

    // Representación de las vidas con imágenes de corazones
    private ArrayList<Image> heartArray;
    private Table leftTable;

    // Mapa de carteles tutoriales (ID -> Etiqueta de texto)
    private Map<Integer, Label> cartelesLabels;

    /**
     * Constructor del HUD.
     * 
     * @param sb SpriteBatch utilizado para renderizar los elementos del HUD.
     */
    public Hud(SpriteBatch sb) {
        // Configuración del viewport y el escenario
        viewport = new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        // Inicialización de variables
        life = 6;
        cherries = 0;
        gems = 0;

        // Cargar la fuente del HUD
        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(1.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Cargar imágenes de corazones para representar vida
        heartArray = new ArrayList<Image>();
        Texture heart = AssetsManager.getTexture("hud/heart.png");
        for (int i = 6; i >= 0; i--) {
            heartArray.add(new Image(new TextureRegion(heart, 0, i * 11, heart.getWidth(), heart.getHeight() / 7)));
        }

        // Cargar imágenes de cereza y gema y Crear etiquetas para mostrar la cantidad
        // de cerezas y gemas
        cherry = (new Image(new TextureRegion(AssetsManager.getTexture("items/cherry.png"), 0, 0, 21, 21)));
        gem = (new Image(new TextureRegion(AssetsManager.getTexture("items/gem.png"), 0, 0, 15, 15)));
        lblCherries = new Label(String.format("x%02d", cherries), labelStyle);
        lblGems = new Label(String.format("x%02d", gems), labelStyle);

        // Crear la tabla principal para organizar el HUD
        Table rootTable = new Table();
        rootTable.top();
        rootTable.setFillParent(true);

        // Tabla izquierda para mostrar las vidas
        leftTable = new Table();
        leftTable.add(heartArray.get(life)).size(99, 25).pad(5).left();

        // Tabla derecha para mostrar cerezas y gemas
        Table rightTable = new Table();
        rightTable.top().right().padTop(5);
        rightTable.add(lblCherries).right().padRight(10);
        rightTable.add(cherry).size(25, 25).padRight(10).row();
        rightTable.add(lblGems).right().padRight(10);
        rightTable.add(gem).size(25, 25).padRight(10);

        // Agregar tablas al HUD
        rootTable.add(leftTable).expandX().left();
        rootTable.add().expandX();
        rootTable.add(rightTable).expandX().right();
        stage.addActor(rootTable);

        // Inicializar los carteles del tutorial
        cartelesLabels = new HashMap<>();
        agregarCartel(1, LanguageManager.get("sign_1"), 275, 200);
        agregarCartel(2, LanguageManager.get("sign_2"), 300, 250);
        agregarCartel(3, LanguageManager.get("sign_3"), 150, 250);
        agregarCartel(4, LanguageManager.get("sign_4"), 150, 300);
        agregarCartel(5, LanguageManager.get("sign_5"), 100, 100);
        agregarCartel(6, LanguageManager.get("sign_6"), 200, 250);

    }

    /**
     * Actualiza el HUD con los nuevos valores de vidas, cerezas y gemas.
     * 
     * @param newLife     Número actualizado de vidas.
     * @param newCherries Número actualizado de cerezas recogidas.
     * @param newGems     Número actualizado de gemas recogidas.
     */
    public void updateHud(int newLife, int newCherries, int newGems) {
        lblCherries.setText(String.format("x%02d", newCherries));
        lblGems.setText(String.format("x%02d", newGems));

        // Actualiza la visualización de la vida
        if (newLife >= 0 && newLife < heartArray.size()) {
            leftTable.clearChildren();
            leftTable.add(heartArray.get(newLife)).size(99, 25).pad(5).left();
        }
    }

    /**
     * Agrega un cartel al HUD.
     * 
     * @param id      Identificador único del cartel.
     * @param mensaje Mensaje a mostrar en el cartel.
     * @param x       Posición X del cartel en la pantalla.
     * @param y       Posición Y del cartel en la pantalla.
     */
    private void agregarCartel(int id, String mensaje, float x, float y) {
        Label label = createLabel(mensaje);
        label.setVisible(false); // Oculto por defecto
        label.setPosition(x, y);
        cartelesLabels.put(id, label);
        stage.addActor(label);
    }

    /**
     * Muestra un cartel del HUD.
     * 
     * @param id Identificador del cartel a mostrar.
     */
    public void showCartel(int id) {
        if (cartelesLabels.containsKey(id)) {
            cartelesLabels.get(id).setVisible(true);
        }
    }

    /**
     * Oculta un cartel del HUD.
     * 
     * @param id Identificador del cartel a ocultar.
     */
    public void hideCartel(int id) {
        if (cartelesLabels.containsKey(id)) {
            cartelesLabels.get(id).setVisible(false);
        }
    }

    /**
     * Libera los recursos utilizados por el HUD.
     */
    @Override
    public void dispose() {
        font.dispose();
        littleFont.dispose();
        stage.dispose();
    }

    /**
     * Crea una etiqueta estilizada para los carteles del HUD.
     * 
     * @param text Texto a mostrar en la etiqueta.
     * @return Una etiqueta estilizada con fondo y fuente personalizada.
     */
    private Label createLabel(String text) {
        // Cargar la imagen de fondo
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(
                AssetsManager.getTexture("hud/backLabel.png"));
        littleFont = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        littleFont.getData().setScale(0.6f);

        // Crear estilo de la etiqueta
        Label.LabelStyle style = new Label.LabelStyle();
        style.background = backgroundDrawable; // Asignar la imagen como fondo
        style.font = littleFont; // Asignar la fuente

        // Crear la etiqueta con alineación centra
        Label label = new Label(text, style);
        label.setSize(label.getPrefWidth() + 40, label.getPrefHeight());
        label.setAlignment(1);
        System.out.println(label.getPrefWidth());
        return label;
    }
}
