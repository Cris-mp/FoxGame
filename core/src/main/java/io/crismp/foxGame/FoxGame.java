//NOTE: PARALLAX: debo renderizar las capas del mapa por separado. Ver:https://libgdx.com/wiki/graphics/2d/tile-maps

package io.crismp.foxGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.AssetsManagerAudio;
import io.crismp.foxGame.managers.LanguageManager;
import io.crismp.foxGame.screens.SplashScreen;
import io.crismp.foxGame.tools.GamePreferences;

public class FoxGame extends Game {

    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0; // 0000000000000000
    public static final short GROUND_BIT = 1; // 0000000000000001
    public static final short FLOOR_BIT = 2; // 0000000000000010
    public static final short WALL_BIT = 4; // 0000000000000100
    public static final short OBSTACLE_BIT = 8; // 0000000000001000

    public static final short FOX_BIT = 16; // 0000000000010000
    public static final short FOX_HEAD_BIT = 32; // 0000000000100000

    public static final short ENEMY_BIT = 64; // 0000000001000000
    public static final short ENEMY_HEAD_BIT = 128; // 0000000010000000

    public static final short LADDER_BIT = 256; // 0000000100000000
    public static final short PINCHOS_BIT = 512; // 0000001000000000
    public static final short ITEM_BIT = 1024; // 0000010000000000
    public static final short END_GAME_BIT = 2048; // 0000100000000000

    public SpriteBatch batch;
    private Music currentMusic; // Para evitar reinicios innecesarios de la música
    public Sound clickSound, clickSound2, clickSound3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        AssetsManager.load();
        AssetsManagerAudio.load();
        AssetsManager.finishLoading();
        AssetsManagerAudio.finishLoading();

        // Cargar preferencias del juego
        GamePreferences.load();
        LanguageManager.loadLanguage();
    

        clickSound = AssetsManagerAudio.getSound("audio/sounds/ui/Wood1.wav");
        clickSound2 = AssetsManagerAudio.getSound("audio/sounds/ui/Wood2.wav");
        clickSound3 = AssetsManagerAudio.getSound("audio/sounds/ui/Wood3.wav");

        Gdx.app.postRunnable(() -> setScreen(new SplashScreen(this)));
    }

    public void playMusic(String musicPath, boolean loop) {
        Music newMusic = AssetsManagerAudio.getMusic(musicPath);

        // Si es la misma música, solo actualizar volumen
        if (currentMusic == newMusic && currentMusic.isPlaying()) {
            updateMusicVolume();
            return;
        }

        // Detener la música anterior si hay alguna
        if (currentMusic != null) {
            currentMusic.stop();
        }

        // Configurar nueva música
        currentMusic = newMusic;
        currentMusic.setLooping(loop);
        updateMusicVolume(); // 📌 Aplica el volumen correcto

        if (GamePreferences.getMusicVolume() > 0) {
            currentMusic.play();
        }
    }

    public void updateMusicVolume() {
        if (currentMusic != null) {
            float volume = GamePreferences.getMusicVolume();
            currentMusic.setVolume(volume);

            // Si el volumen es 0, pausar la música; si es mayor, reanudarla
            if (volume == 0) {
                currentMusic.pause();
            } else if (!currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    public void playSound(Sound sound) {
        if (GamePreferences.getSoundVolume() > 0) {
            sound.play(GamePreferences.getSoundVolume());
        }
    }

    @Override
    public void render() {
        super.render();
    }

    public void dispose() {
        super.dispose();
        AssetsManager.dispose();
        AssetsManagerAudio.dispose();
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        batch.dispose();
    }

}
