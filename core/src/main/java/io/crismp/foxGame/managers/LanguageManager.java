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
        String langCode = prefs.getString("language", "es");  // Por defecto "es" (Español)
        Locale locale = new Locale(langCode);  // Configurar el idioma con el código

        try {
            // Asegúrate de que el archivo de idioma esté en la ruta correcta
            // Aquí estamos cargando el archivo según el idioma especificado en el prefs
            myBundle = I18NBundle.createBundle(Gdx.files.internal("languages/" + langCode), locale);
            System.out.println("Idioma cargado correctamente: " + langCode); // Mensaje de confirmación
        } catch (Exception e) {
            System.err.println("Error al cargar el idioma: " + langCode); // Si ocurre un error
            e.printStackTrace();
        }
    }

    // Obtener traducción de una clave
    public static String get(String key) {
        return myBundle.get(key);
    }

    // Cambiar idioma manualmente
    public static void setLanguage(String langCode) {
        System.out.println("Entro enleng"+langCode);
        prefs.putString("language", langCode);
        prefs.flush();
        loadLanguage(); // Recargar el idioma después de guardarlo
    }
    // private static I18NBundle myBundle;
    // private static Preferences prefs = Gdx.app.getPreferences("FoxGamePrefs");

    // // Cargar idioma actual
    // public static void loadLanguage() {
    //     Locale locale;
    //     String langCode;

    //     if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
    //         langCode = prefs.getString("language", Locale.getDefault().getLanguage());
    //         locale = new Locale(langCode);
    //     } else {
    //         langCode = prefs.getString("language", "es");
    //     }
    //     locale = new Locale(langCode);

    //     myBundle = I18NBundle.createBundle(Gdx.files.internal("languages/espanol"), locale);
    //     System.out.println("locale"+ locale);
    // }

    // // Obtener traducción de una clave
    // public static String get(String key) {
    //     return myBundle.get(key);
    // }

    // // Cambiar idioma manualmente
    // public static void setLanguage(String langCode) {
    //     prefs.putString("language", langCode);
    //     prefs.flush();
    //     loadLanguage();
    // }
}
// public class LanguageManager {
//     private static Properties languageProperties = new Properties();

//     public static void loadLanguage(String languageCode) {
//         String filePath = "languages/" + (languageCode.equals("Ingles")?"lang_en":"lang_es") + ".properties";
//         FileHandle file = Gdx.files.internal(filePath);

//         if (file.exists()) {
//             try {
//                 languageProperties.load(file.read());
//                 GamePreferences.setLanguage(languageCode);  // Guardar el idioma en preferencias
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         } else {
//             Gdx.app.log("LanguageManager", "Archivo de idioma no encontrado: " + filePath);
//         }
//     }

//     public static String get(String key) {
//         return languageProperties.getProperty(key, key);  // Retorna la clave si no existe traducción
//     }
// }
