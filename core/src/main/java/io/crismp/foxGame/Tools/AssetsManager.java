package io.crismp.foxGame.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetsManager {

    public static final AssetManager manager = new AssetManager();

    public static void load() {
        //------TEXTURES------
        //ENEMIES
        manager.load("enemies/enemy-deadth.png", Texture.class);
        manager.load("enemies/opossum.png", Texture.class);
        //ITEMS
        manager.load("items/cherry.png", Texture.class);
        manager.load("items/gem.png", Texture.class);
        //JOYSTICK
        manager.load("joystick/boton.png", Texture.class);
        manager.load("joystick/botonPress.png", Texture.class);
        manager.load("joystick/Joystick.png", Texture.class);
        manager.load("joystick/SmallHandleFilledGrey.png", Texture.class);
        //PLAYER
        manager.load("player/zorrito.png", Texture.class);
        //UI
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
        manager.load("ui/sonidos.png", Texture.class);
        manager.load("ui/sonidos_pulsado.png", Texture.class);
        manager.load("ui/title.png", Texture.class);
        //HUD
        manager.load("hud/heart.png", Texture.class);

        //------FONTS------
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
