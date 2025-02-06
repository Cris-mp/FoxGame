package io.crismp.foxGame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
    private static final String PREFS_NAME = "foxGamePrefs";  // Nombre del archivo de preferencias
    private static final String MUSIC_VOLUME = "musicVolume";
    private static final String SOUND_VOLUME = "soundVolume";
    private static final String VIBRATION = "vibration";
    private static final String LANGUAGE = "language";
    private static final String HIGH_SCORE = "highScore";

    private static Preferences prefs;

    // Inicializar preferencias
    public static void load() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    // Guardar volumen de música
    public static void setMusicVolume(float volume) {
        prefs.putFloat(MUSIC_VOLUME, volume);
        prefs.flush();
    }

    public static float getMusicVolume() {
        return prefs.getFloat(MUSIC_VOLUME, 1.0f);  // Valor por defecto: 1.0
    }

    // Guardar volumen de sonido
    public static void setSoundVolume(float volume) {
        prefs.putFloat(SOUND_VOLUME, volume);
        prefs.flush();
    }

    public static float getSoundVolume() {
        return prefs.getFloat(SOUND_VOLUME, 1.0f);
    }

    // Guardar si la vibración está activada
    public static void setVibration(boolean enabled) {
        prefs.putBoolean(VIBRATION, enabled);
        prefs.flush();
    }

    public static boolean isVibrationEnabled() {
        return prefs.getBoolean(VIBRATION, true);  // Por defecto activado
    }

    // Guardar idioma
    public static void setLanguage(String language) {
        prefs.putString(LANGUAGE, language);
        prefs.flush();
    }

    public static String getLanguage() {
        return prefs.getString(LANGUAGE, "Espanol");  // Español por defecto
    }

    // Guardar record de puntuación
    public static void setHighScore(int score) {
        prefs.putInteger(HIGH_SCORE, score);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger(HIGH_SCORE, 0);  // Valor por defecto: 0
    }
}
