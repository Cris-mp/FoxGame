package io.crismp.foxGame.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AssetsManagerAudio {
    private static final AssetManager manager = new AssetManager();

    public static void load() {
        manager.load("audio/music/exploration.ogg", Music.class);
        manager.load("audio/music/joyful.ogg", Music.class);
        manager.load("audio/music/world wanderer.ogg", Music.class);
        manager.load("audio/music/To Suffer a Loss (Game Over).ogg", Music.class);
        manager.load("audio/music/Victorious.ogg", Music.class);
        manager.load("audio/sounds/ui/Wood1.wav", Sound.class);
        manager.load("audio/sounds/ui/Wood2.wav", Sound.class);
        manager.load("audio/sounds/ui/Wood3.wav", Sound.class);
        
    }
    public static void finishLoading() {
        manager.finishLoading();
    }

    public static Music getMusic(String fileName) {
        return manager.get(fileName, Music.class);
    }

    public static Sound getSound(String fileName) {
        return manager.get(fileName, Sound.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}
