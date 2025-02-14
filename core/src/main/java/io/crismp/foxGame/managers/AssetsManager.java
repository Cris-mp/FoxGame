package io.crismp.foxGame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Administra la carga, almacenamiento y disposición de los recursos del juego,
 * como texturas y fuentes. Utiliza el objeto `AssetManager` de libGDX para
 * gestionar la carga de los activos de forma asíncrona.
 * 
 * Esta clase proporciona métodos para cargar las texturas necesarias,
 * obtenerlas cuando se necesiten,y limpiar los recursos cuando ya no son
 * necesarios.
 * 
 * Se carga una serie de activos como texturas para enemigos, objetos,
 * controles, la interfaz de usuario (UI), el jugador, entre otros, y también
 * fuentes para texto.
 */
public class AssetsManager {

    public static final AssetManager manager = new AssetManager();

    /**
     * Carga todos los recursos (texturas y fuentes) necesarios para el juego.
     * Este método se encarga de añadir los activos a la cola de carga del
     * AssetManager.
     */
    public static void load() {
        // ------TEXTURES------
        // ENEMIES
        manager.load("enemies/enemy-deadth.png", Texture.class);
        manager.load("enemies/opossum.png", Texture.class);
        // ITEMS
        manager.load("items/cherry.png", Texture.class);
        manager.load("items/gem.png", Texture.class);
        // JOYSTICK
        manager.load("joystick/boton.png", Texture.class);
        manager.load("joystick/botonPress.png", Texture.class);
        manager.load("joystick/Joystick.png", Texture.class);
        manager.load("joystick/SmallHandleFilledGrey.png", Texture.class);
        // PLAYER
        manager.load("player/zorrito.png", Texture.class);
        // UI
        manager.load("ui/background.png", Texture.class);
        manager.load("ui/backSettings.png", Texture.class);
        manager.load("ui/boton_cua.png", Texture.class);
        manager.load("ui/boton_cua_pulsado.png", Texture.class);
        manager.load("ui/boton_p.png", Texture.class);
        manager.load("ui/boton_p_pulsado.png", Texture.class);
        manager.load("ui/boton.png", Texture.class);
        manager.load("ui/boton_pulsado.png", Texture.class);
        manager.load("ui/mainMenu.png", Texture.class);
        manager.load("ui/mainMenu_pulsado.png", Texture.class);
        manager.load("ui/musica.png", Texture.class);
        manager.load("ui/musica_pulsado.png", Texture.class);
        manager.load("ui/opciones.png", Texture.class);
        manager.load("ui/opciones_pulsado.png", Texture.class);
        manager.load("ui/records.png", Texture.class);
        manager.load("ui/records_pulsado.png", Texture.class);
        manager.load("ui/slide.png", Texture.class);
        manager.load("ui/slide_b.png", Texture.class);
        manager.load("ui/sonidos.png", Texture.class);
        manager.load("ui/sonidos_pulsado.png", Texture.class);
        manager.load("ui/splashBack.png", Texture.class);
        manager.load("ui/splash_press.png", Texture.class);
        manager.load("ui/title.png", Texture.class);
        manager.load("ui/vibracion.png", Texture.class);
        manager.load("ui/vibracion_pulsado.png", Texture.class);
        // HUD
        manager.load("hud/backLabel.png", Texture.class);
        manager.load("hud/heart.png", Texture.class);
        manager.load("hud/skull2.png", Texture.class);

        // ------FONTS------
        manager.load("fonts/wood.fnt", BitmapFont.class);
    }

    /**
     * Finaliza la carga de todos los recursos del AssetManager. Este método bloquea
     * el hilo de ejecución hasta que todos los recursos estén completamente
     * cargados.
     */
    public static void finishLoading() {
        manager.finishLoading();
    }

    /**
     * Obtiene la textura cargada en el AssetManager utilizando la ruta
     * especificada.
     * 
     * @param path La ruta del recurso (en formato de cadena) que se desea obtener.
     * @return La textura cargada en el AssetManager.
     */
    public static Texture getTexture(String path) {
        return manager.get(path, Texture.class);
    }

    /**
     * Obtiene la fuente (BitmapFont) cargada en el AssetManager utilizando la ruta
     * especificada.
     * 
     * @param path La ruta del recurso (en formato de cadena) que se desea obtener.
     * @return La fuente cargada en el AssetManager.
     */
    public static BitmapFont getFont(String path) {
        return manager.get(path, BitmapFont.class);
    }

    /**
     * Libera los recursos cargados en el AssetManager. Este método debe ser llamado
     * cuando ya no se necesiten
     * los recursos cargados (por ejemplo, al cerrar o cambiar de pantalla).
     */
    public static void dispose() {
        manager.dispose();
    }
}
