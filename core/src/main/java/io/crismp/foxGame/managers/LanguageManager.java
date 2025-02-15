package io.crismp.foxGame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Clase encargada de la gestión del idioma en el juego.
 * 
 * Permite cargar, obtener y cambiar el idioma dinámicamente usando archivos de
 * localización.
 * Guarda la preferencia del usuario en `Preferences` para mantener el idioma
 * seleccionado
 * entre sesiones.
 */
public class LanguageManager {

    /** Bundle que almacena las traducciones cargadas para el idioma actual. */
    private static I18NBundle myBundle;

    /** Preferencias para almacenar la configuración del idioma. */
    private static final Preferences prefs = Gdx.app.getPreferences("FoxGamePrefs");

    /** Código del idioma actual (para evitar recargas innecesarias). */
    private static String currentLangCode = "es";

    /**
     * Carga el idioma actual desde las preferencias del usuario.
     * Si el idioma no está configurado, usa español ("es") por defecto.
     */
    public static void loadLanguage() {
        String langCode = prefs.getString("language", "es");
        Locale locale = Locale.forLanguageTag(langCode); // Maneja mejor códigos como "en-US"

        try {
            myBundle = I18NBundle.createBundle(Gdx.files.internal("languages/" + langCode), locale);
            currentLangCode = langCode; // Actualizar idioma actual
        } catch (Exception e) {
            System.err.println("Error al cargar el idioma: " + langCode);
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la traducción de una clave específica del archivo de localización.
     * 
     * @param key Clave del texto traducido (ejemplo: `"menu.play"`).
     * @return Traducción correspondiente al idioma actual o un mensaje indicando
     *         que falta la clave.
     */
    public static String get(String key) {
        if (myBundle == null) {
            System.err.println("Intento de acceso a un bundle de idioma no cargado.");
            return "[LANGUAGE NOT LOADED]";
        }
        try {
            return myBundle.get(key);
        } catch (Exception e) {
            return "[MISSING: " + key + "]";
        }
    }

    /**
     * Cambia el idioma del juego y guarda la preferencia para futuras sesiones.
     * 
     * @param langCode Código del idioma en formato ISO 639-1 (ejemplo: `"en"` para
     *                 inglés, `"es"` para español).
     */
    public static void setLanguage(String langCode) {
        if (!currentLangCode.equals(langCode)) { // Evita recargar si ya está en ese idioma
            prefs.putString("language", langCode);
            prefs.flush(); // Guardar cambios
            loadLanguage(); // Recargar idioma
        }
    }
}