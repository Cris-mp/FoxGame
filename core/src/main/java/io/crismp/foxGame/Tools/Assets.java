package io.crismp.foxGame.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
    //TODO: Hacer pruebas con esto
    public static final AssetManager manager = new AssetManager();

    public static void load() {
        // Cargar imágenes del menú
        manager.load("ui/background.png", Texture.class);
        manager.load("ui/title.png", Texture.class);
        manager.load("ui/boton.png", Texture.class);
        manager.load("ui/boton_pulsado.png", Texture.class);
        manager.load("ui/musica.png", Texture.class);
        manager.load("ui/musica_pulsado.png", Texture.class);
        manager.load("ui/sonidos.png", Texture.class);
        manager.load("ui/sonidos_pulsado.png", Texture.class);
        manager.load("ui/boton_cua.png", Texture.class);
        manager.load("ui/boton_cua_pulsado.png", Texture.class);
        manager.load("ui/backSettings.png", Texture.class);

        // Cargar fuente
        manager.load("fonts/wood.fnt", BitmapFont.class);
    }

    public static void finishLoading() {
        manager.finishLoading();
    }

    public static Texture getTexture(String path) {
        return manager.get(path, Texture.class);
    }

    public static BitmapFont getFont(String path) {
        return manager.get(path, BitmapFont.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}

/**
 * seica es asi pero no me va
 * backgroundTexture = new Texture("ui/background.png");
🔹 Después (con Assets):

backgroundTexture = Assets.getTexture("ui/background.png");
También en los botones:

btnNormal = new TextureRegionDrawable(Assets.getTexture("ui/boton.png"));
btnPressed = new TextureRegionDrawable(Assets.getTexture("ui/boton_pulsado.png"));
 */