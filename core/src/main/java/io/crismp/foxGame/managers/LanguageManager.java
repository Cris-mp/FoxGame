package io.crismp.foxGame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class LanguageManager {
    private static I18NBundle myBundle;
    private static Preferences prefs = Gdx.app.getPreferences("FoxGamePrefs");

    // Cargar idioma actual
    public static void loadLanguage() {
        String langCode = prefs.getString("language", "es");
        Locale locale = new Locale(langCode);
        try {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("languages/" + langCode), locale);
        } catch (Exception e) {
            System.err.println("Error al cargar el idioma: " + langCode);
            e.printStackTrace();
        }
    }

    // Obtener traducción de una clave
    public static String get(String key) {
        return myBundle.get(key);
    }

    // Cambiar idioma manualmente
    public static void setLanguage(String langCode) {
        System.out.println("Entro enleng" + langCode);
        prefs.putString("language", langCode);
        prefs.flush();
        loadLanguage();
    }
}
