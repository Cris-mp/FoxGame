package io.crismp.foxGame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
    private static final String PREFS_NAME = "foxGamePrefs";  // Nombre del archivo de preferencias
    private static final String MUSIC_VOLUME = "musicVolume";
    private static final String SOUND_VOLUME = "soundVolume";
    private static final String VIBRATION = "vibration";
    private static final String LANGUAGE = "language";
    private static final String HIGH_SCORES = "highScores";

    private static Preferences prefs;

    // Inicializar preferencias
    public static void load() {
        if (prefs == null) {  // 💡 Evita problemas de inicialización
            prefs = Gdx.app.getPreferences(PREFS_NAME);
        }
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
        load();
        prefs.putString(LANGUAGE, language);
        prefs.flush();
    }

    public static String getLanguage() {
        load();
        return prefs.getString(LANGUAGE, "es");  // Español por defecto
    }

     // 🔹 Guardar nueva puntuación, manteniendo solo las 5 mejores
     public static void saveScore(int newScore) {
        load();
        String scoresString = prefs.getString(HIGH_SCORES, "0,0,0,0,0");
        String[] scoresArray = scoresString.split(",");
    
        // Convertimos a enteros
        int[] scores = new int[scoresArray.length];
        for (int i = 0; i < scoresArray.length; i++) {
            scores[i] = Integer.parseInt(scoresArray[i]);
        }
    
        // Verificar si la nueva puntuación es mejor que la más baja (última del ranking)
        if (newScore > scores[scores.length - 1]) {
            scores[scores.length - 1] = newScore; // Reemplazar solo si es mayor
            java.util.Arrays.sort(scores); // Ordenar ascendente
            reverseArray(scores); // Invertir para que queden en orden descendente
    
            // Guardar en Preferences
            StringBuilder newScores = new StringBuilder();
            for (int i = 0; i < scores.length; i++) {
                newScores.append(scores[i]);
                if (i < scores.length - 1) newScores.append(",");
            }
    
            prefs.putString(HIGH_SCORES, newScores.toString());
            prefs.flush(); // Guardar cambios
        }
    }

    // 🔹 Obtener las 5 mejores puntuaciones como un array de enteros
    public static int[] getHighScores() {
        load();
        String scoresString = prefs.getString(HIGH_SCORES, "0,0,0,0,0");
        String[] scoresArray = scoresString.split(",");
        int[] scores = new int[scoresArray.length];

        for (int i = 0; i < scoresArray.length; i++) {
            scores[i] = Integer.parseInt(scoresArray[i]);
        }

        return scores;
    }

    // 🔹 Método auxiliar para invertir el array (ya que Arrays.sort() es ascendente)
    private static void reverseArray(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
