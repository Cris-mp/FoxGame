package io.crismp.foxGame.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    public static final AssetManager manager = new AssetManager();

    public static void load() {
        //-----MUSIC-----

        //-----SOUND-----

    }

    public static void finishLoading() {
        manager.finishLoading();
    }

    public static Music getTexture(String path) {
        return manager.get(path, Music.class);
    }

    public static Sound getFont(String path) {
        return manager.get(path, Sound.class);
    }

    public static void dispose() {
        manager.dispose();
    }

}
