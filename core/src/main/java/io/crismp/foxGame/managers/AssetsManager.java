package io.crismp.foxGame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Clase encargada de gestionar los recursos del juego, como texturas y fuentes.
 * 
 * Utiliza `AssetManager` de libGDX para cargar los recursos de forma eficiente
 * y
 * asíncrona, evitando bloqueos en el juego. Se encarga de:
 * - Cargar texturas y fuentes necesarias.
 * - Recuperarlas cuando sean necesarias.
 * - Liberar memoria cuando ya no se requieran.
 */
public class AssetsManager {
    /**
     * Instancia única del `AssetManager` utilizada para manejar los recursos.
     */
    public static final AssetManager manager = new AssetManager();

    /**
     * Carga todos los recursos del juego y los añade a la cola de carga del
     * `AssetManager`.
     * 
     * Se incluyen texturas de enemigos, ítems, joystick, jugador, interfaz de
     * usuario (UI),
     * botones, splash screen, HUD y fuentes tipográficas.
     */
    public static void load() {
        // ------TEXTURAS------
        // **Enemigos**
        manager.load("enemies/enemy-deadth.png", Texture.class);
        manager.load("enemies/opossum.png", Texture.class);
        // **Objetos del juego (Items)**
        manager.load("items/cherry.png", Texture.class);
        manager.load("items/gem.png", Texture.class);
        // **Joystick (Controles)**
        manager.load("joystick/boton.png", Texture.class);
        manager.load("joystick/botonPress.png", Texture.class);
        manager.load("joystick/Joystick.png", Texture.class);
        manager.load("joystick/SmallHandleFilledGrey.png", Texture.class);
        // **Jugador**
        manager.load("player/zorrito.png", Texture.class);
        // **Interfaz de usuario (UI)**
        manager.load("ui/background.png", Texture.class);
        manager.load("ui/backSettings.png", Texture.class);
        manager.load("ui/title.png", Texture.class);
        // **Botones**
            // manager.load("ui/button/btnRect.png", Texture.class);
            // manager.load("ui/button/btnRect_p.png", Texture.class);
            // manager.load("ui/button/btnCuadrado.png", Texture.class);
            // manager.load("ui/button/btnCuadrado_p.png", Texture.class);

        manager.load("ui/button/btnOval.png", Texture.class);
        manager.load("ui/button/btnOval_p.png", Texture.class);
        manager.load("ui/button/btnOptions.png", Texture.class);
        manager.load("ui/button/btnOptions_p.png", Texture.class);
        manager.load("ui/button/btnMainMenu.png", Texture.class);
        manager.load("ui/button/btnMainMenu_p.png", Texture.class);
        manager.load("ui/button/btnRecords.png", Texture.class);
        manager.load("ui/button/btnRecords_p.png", Texture.class);
        // **Botones de configuración**
        manager.load("ui/button/tglMusic_on.png", Texture.class);
        manager.load("ui/button/tglMusic_off.png", Texture.class);
        manager.load("ui/button/tglSound_on.png", Texture.class);
        manager.load("ui/button/tglSound_off.png", Texture.class);
        manager.load("ui/button/tglVibration_on.png", Texture.class);
        manager.load("ui/button/tglVibration_off.png", Texture.class);
        // **Slider para configuraciones**
        manager.load("ui/button/btnSlide.png", Texture.class);
        manager.load("ui/button/backSlide.png", Texture.class);

        // **Pantalla de inicio (Splash)**
        manager.load("ui/splash/splashBack.png", Texture.class);
        manager.load("ui/splash/splash_press.png", Texture.class);
        manager.load("ui/splash/splash_pulsa.png", Texture.class);

        // **HUD (Head-Up Display)**
        manager.load("hud/backLabel.png", Texture.class);
        manager.load("hud/heart.png", Texture.class);
        manager.load("hud/skull2.png", Texture.class);

        // ------ FUENTES ------
        manager.load("fonts/wood.fnt", BitmapFont.class);
    }

    /**
     * Finaliza la carga de todos los recursos de manera síncrona.
     * 
     * Este método bloquea la ejecución del juego hasta que todos los recursos
     * estén completamente cargados, por lo que solo debe usarse en pantallas de
     * carga donde se espere este comportamiento.
     */
    public static void finishLoading() {
        manager.finishLoading();
    }

    /**
     * Obtiene una textura específica que ha sido previamente cargada en el
     * `AssetManager`.
     * 
     * @param path Ruta del recurso en formato de cadena (ejemplo:
     *             `"items/cherry.png"`).
     * @return La textura correspondiente a la ruta especificada.
     * @throws IllegalArgumentException si la textura no se ha cargado previamente.
     */
    public static Texture getTexture(String path) {
        return manager.get(path, Texture.class);
    }

    /**
     * Obtiene una fuente (BitmapFont) específica que ha sido previamente cargada en
     * el `AssetManager`.
     * 
     * @param path Ruta de la fuente en formato de cadena (ejemplo:
     *             `"fonts/wood.fnt"`).
     * @return La fuente correspondiente a la ruta especificada.
     * @throws IllegalArgumentException si la fuente no se ha cargado previamente.
     */
    public static BitmapFont getFont(String path) {
        return manager.get(path, BitmapFont.class);
    }

    /**
     * Libera todos los recursos cargados en el `AssetManager`, liberando la memoria
     * ocupada.
     * 
     * Este método debe llamarse al cerrar el juego o al cambiar de pantalla si los
     * recursos ya no son necesarios, para evitar fugas de memoria.
     */
    public static void dispose() {
        manager.dispose();
    }
}
