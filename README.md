# foxGame
# 🦊 Fox Raider  

**Fox Raider** es un emocionante juego de plataformas 2D en el que controlas a **Foxy**, un astuto zorro que debe superar obstáculos, enemigos y descubrir zonas secretas para completar niveles y obtener la máxima puntuación.  

## 📜 Características principales  
✔️ Movimiento fluido con física basada en Box2D  
✔️ Enemigos con IA simple y obstáculos interactivos  
✔️ Coleccionables como gemas y cerezas para aumentar la puntuación  
✔️ Habitaciones secretas que se revelan dinámicamente  
✔️ Sistema de puntuación y récords acumulados  
✔️ Guardado de progresos y configuración de usuario  
✔️ Compatible con controladores y teclado  

---

## 🎮 Controles  

| Acción                | Tecla               |
|----------------------|------------------|
| Moverse izquierda/derecha | ← / →           |
| Saltar               | Barra espaciadora |
| Trepar escaleras     | ↑ / ↓             |
| Entrar en zonas secretas | Automático       |
| Interactuar          | E                 |
| Pausar              | ESC               |

---

## 📥 Instalación y ejecución  

### **Requisitos**  
- **Java 17** o superior  
- **LibGDX** y dependencias instaladas  
- **Base de datos MariaDB** si se usa funcionalidad online  
- **Sistema operativo:** Windows, Linux o macOS  

### **Ejecutar el juego**  
1. Clona este repositorio:  
   ```sh
   git clone https://github.com/tuusuario/FoxGame.git
   cd FoxGame

🛠️ Desarrollo y arquitectura
### Estructura del proyecto
FoxGame/
│── android/         # Versión para Android
│── assets/          # Texturas, sonidos y mapas de Tiled
│── core/            # Código fuente principal
│── desktop/         # Versión de escritorio
│── sql/             # Esquemas y scripts para la base de datos
│── README.md        # Este archivo
│── build.gradle     # Configuración del proyecto
### Principales clases y funcionalidades
## 🎮 Lógica del juego
Foxy.java → Control del personaje principal (movimiento, animaciones, colisiones).
Enemy.java → Base para enemigos con detección de colisiones.
Pinchos.java → Obstáculos que dañan a Foxy.
Item.java → Sistema de coleccionables (Gemas, cerezas, etc.).
## 🗺️ Gestión de niveles
PlayScreen.java → Carga de niveles y HUD.
B2WorldCreator.java → Generación del mundo a partir del mapa de Tiled.
WorldContactListener.java → Manejo de colisiones entre objetos.
## 💾 Sistema de puntuación y guardado
GamePreferences.java → Manejo de puntuaciones, configuración y progresos.
FinalLevelScreen.java → Pantalla de fin de nivel y guardado de récords.
---
### 🔥 Mecánicas avanzadas
## 🌟 Puntuación acumulada
✔️ Se guarda solo si completas un nivel.
✔️ Evita duplicaciones de puntuación.
✔️ Si mueres en el segundo nivel, solo se guarda la del primero.

## 🔑 Habitaciones secretas
✔️ Se activan y desactivan dinámicamente.
✔️ Pueden contener enemigos y coleccionables.

## ⚖️ Física de rampas y escaleras
✔️ Implementada con Box2D para movimiento realista.
✔️ Se gestiona en WorldContactListener.java.

## 🆚 IA básica de enemigos
✔️ Cambian de dirección al chocar con paredes u obstáculos.
✔️ Se invoca en Enemy.java con la función reverseVelocity().
---
## 🚀 Planes futuros
🔹 Más niveles y enemigos
---
## 📧 Contacto: 
krismipi@gmail.com
---

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
