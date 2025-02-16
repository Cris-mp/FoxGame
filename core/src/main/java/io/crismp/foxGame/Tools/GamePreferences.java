package io.crismp.foxGame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que maneja las preferencias del juego, como volumen, idioma, vibración y puntuaciones altas.
 */
public class GamePreferences {
    private static final String PREFS_NAME = "foxGamePrefs";  // Nombre del archivo de preferencias
    private static final String MUSIC_VOLUME = "musicVolume";
    private static final String SOUND_VOLUME = "soundVolume";
    private static final String VIBRATION = "vibration";
    private static final String LANGUAGE = "language";
    private static final String HIGH_SCORES = "highScores";
    private static final int MAX_SCORES = 5;  // Número máximo de puntuaciones guardadas

    private static Preferences prefs;

    /**
     * Obtiene las preferencias del juego, asegurándose de que estén cargadas.
     *
     * @return Objeto de preferencias.
     */
    private static Preferences getPreferences() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }
        return prefs;
    }

    /**
     * Establece el volumen de la música.
     *
     * @param volume Valor del volumen entre 0.0 y 1.0.
     */
    public static void setMusicVolume(float volume) {
        getPreferences().putFloat(MUSIC_VOLUME, volume).flush();
    }

    /**
     * Obtiene el volumen de la música.
     *
     * @return Valor del volumen (por defecto 1.0 si no ha sido configurado).
     */
    public static float getMusicVolume() {
        return getPreferences().getFloat(MUSIC_VOLUME, 1.0f);
    }

    /**
     * Establece el volumen de los efectos de sonido.
     *
     * @param volume Valor del volumen entre 0.0 y 1.0.
     */
    public static void setSoundVolume(float volume) {
        getPreferences().putFloat(SOUND_VOLUME, volume).flush();
    }

    /**
     * Obtiene el volumen de los efectos de sonido.
     *
     * @return Valor del volumen (por defecto 1.0 si no ha sido configurado).
     */
    public static float getSoundVolume() {
        return getPreferences().getFloat(SOUND_VOLUME, 1.0f);
    }

    /**
     * Activa o desactiva la vibración en el juego.
     *
     * @param enabled {@code true} para activar la vibración, {@code false} para desactivarla.
     */
    public static void setVibration(boolean enabled) {
        getPreferences().putBoolean(VIBRATION, enabled).flush();
    }

    /**
     * Verifica si la vibración está activada.
     *
     * @return {@code true} si la vibración está activada, {@code false} si está desactivada.
     */
    public static boolean isVibrationEnabled() {
        return getPreferences().getBoolean(VIBRATION, true);
    }

    /**
     * Establece el idioma del juego.
     *
     * @param language Código de idioma (ejemplo: "es" para español, "en" para inglés).
     */
    public static void setLanguage(String language) {
        getPreferences().putString(LANGUAGE, language).flush();
    }

    /**
     * Obtiene el idioma configurado en el juego.
     *
     * @return Código de idioma actual (por defecto "es").
     */
    public static String getLanguage() {
        return getPreferences().getString(LANGUAGE, "es");
    }

    /**
     * Guarda una nueva puntuación en la lista de récords, manteniendo solo las 5 mejores puntuaciones.
     *
     * @param newScore La nueva puntuación a evaluar y almacenar si es suficientemente alta.
     */
    public static void saveScore(int newScore) {
        List<Integer> scores = getHighScoresList();

        // Agregar la nueva puntuación
        scores.add(newScore);

        // Ordenar en orden descendente
        scores.sort(Collections.reverseOrder());

        // Mantener solo las 5 mejores
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        // Convertir la lista a String y guardar en preferencias
        String scoresString = scores.toString().replaceAll("[\\[\\] ]", "");  // Formato "100,90,80,70,60"
        getPreferences().putString(HIGH_SCORES, scoresString).flush();
    }

    /**
     * Obtiene las 5 mejores puntuaciones almacenadas.
     *
     * @return Un array de enteros con las 5 mejores puntuaciones.
     */
    public static int[] getHighScores() {
        List<Integer> scores = getHighScoresList();
        return scores.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Obtiene la lista de puntuaciones almacenadas.
     *
     * @return Lista de enteros con las puntuaciones.
     */
    private static List<Integer> getHighScoresList() {
        String scoresString = getPreferences().getString(HIGH_SCORES, "0,0,0,0,0");
        String[] scoresArray = scoresString.split(",");
        List<Integer> scores = new ArrayList<>();

        for (String s : scoresArray) {
            try {
                scores.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                scores.add(0);  // En caso de error, añadir un 0
            }
        }

        return scores;
    }
}