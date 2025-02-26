package io.crismp.foxGame.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManagerAudio;
import io.crismp.foxGame.scenes.Hud;
import io.crismp.foxGame.sprites.Foxy;
import io.crismp.foxGame.sprites.enemies.Enemy;
import io.crismp.foxGame.sprites.enemies.Zarigueya;
import io.crismp.foxGame.sprites.items.Cherry;
import io.crismp.foxGame.sprites.items.Gem;
import io.crismp.foxGame.tools.B2WorldCreator;
import io.crismp.foxGame.tools.VirtualJoystick;
import io.crismp.foxGame.tools.WorldContactListener;

/**
 * Esta clase representa la pantalla de juego, donde el jugador interactúa con
 * el mundo, recolecta objetos, y avanza a través de niveles.
 * Implementa la interfaz `Screen` para manejar los eventos del ciclo de vida de
 * la pantalla.
 */
public class PlayScreen implements Screen {
    public FoxGame game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    public Hud hud;
    private VirtualJoystick joystick;

    private int cherriesCollected;
    private int gemsCollected;
    private int newLife;
    private int nivelActual;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private Foxy player;

    private Sound jump, run;
    private float stepTimer = 0f;
    private float stepInterval = 0.3f;

    private float mapWidthInUnits, mapHeightInUnits;
    private float accumulator;
    private float timeStep;
    public boolean colision;


    // getters
    public FoxGame getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getCherriesCollected() {
        return cherriesCollected;
    }

    public int getGemsCollected() {
        return gemsCollected;
    }


    /**
     * Constructor de la clase PlayScreen.
     * Inicializa los elementos esenciales del juego como el mapa, cámara, jugador,
     * física y HUD.
     *
     * @param game        El juego principal.
     * @param nivelActual El nivel actual en el que se encuentra el jugador.
     */
    public PlayScreen(FoxGame game, int nivelActual) {
        this.game = game;
        this.nivelActual = nivelActual;

        // Inicialización de variables
        this.joystick = new VirtualJoystick(0, 0, 2, 1);
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(FoxGame.V_WIDTH / FoxGame.PPM, FoxGame.V_HEIGHT / FoxGame.PPM, gamecam);
        accumulator = 0f;
        timeStep = 1 / 60f;
        cherriesCollected = 0;
        gemsCollected = 0;
        newLife = 6;
        colision = false;

        // Inicialización de HUD
        hud = new Hud(game.batch);

        // Carga de mapa y música
        mapLoader = new TmxMapLoader();
        loadMapAndMusic();

        renderer = new OrthogonalTiledMapRenderer(map, 1 / FoxGame.PPM);

        // Inicialización de la cámara
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Inicialización del mundo y física
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        // Creación de elementos del mundo (jugador, enemigos, etc.)
        creator = new B2WorldCreator(this);
        player = new Foxy(this);
        world.setContactListener(new WorldContactListener(this));

        // Inicialización de las dimensiones del mapa
        initializeMapDimensions();

        // Carga de sonido
        loadSounds();

    }

    /**
     * Carga el mapa y la música correspondiente según el nivel actual.
     */
    private void loadMapAndMusic() {
        switch (nivelActual) {
            case 0:
                map = mapLoader.load("maps/Tutorial.tmx");
                game.playMusic("audio/music/world wanderer.ogg", true);
                break;
            case 1:
                map = mapLoader.load("maps/Fase1.tmx");
                game.playMusic("audio/music/exploration.ogg", true);
                break;
            case 2:
                map = mapLoader.load("maps/Fase2.tmx");
                game.playMusic("audio/music/dark-happy-world.ogg", true);
                break;
            default:
                map = mapLoader.load("maps/Tutorial.tmx");
                game.playMusic("audio/music/world wanderer.ogg", true);
                break;
        }
    }

    /**
     * Inicializa las dimensiones del mapa en unidades del mundo.
     */
    private void initializeMapDimensions() {
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tileSize = prop.get("tilewidth", Integer.class);

        mapWidthInUnits = (mapWidth * tileSize) / FoxGame.PPM;
        mapHeightInUnits = (mapHeight * tileSize) / FoxGame.PPM;
    }

    /**
     * Carga los sonidos necesarios para el juego.
     */
    private void loadSounds() {
        jump = AssetsManagerAudio.getSound("audio/sounds/player/jump.ogg");
        run = AssetsManagerAudio.getSound("audio/sounds/player/Step_rock.ogg");
    }

    public void handleInput(float dt) {
        if (player.currenState != Foxy.State.DEAD) {
            player.velX = 0;
            if (Gdx.app.getType() == ApplicationType.Android) {
                handleAndroidInput(dt);
            } else {
                handleDesktopInput(dt);
            }
        }
    }

    /**
     * Maneja la entrada en dispositivos Android.
     * 
     * @param dt El tiempo delta.
     */
    private void handleAndroidInput(float dt) {
        if (joystick.isJumpPressed() && player.jumpCounter < 2) {
            game.playSound(jump);
            player.body.setLinearVelocity(0, 0);
            player.body.applyLinearImpulse(new Vector2(0, 2.5f), player.body.getWorldCenter(), true);
            player.jumpCounter++;
            joystick.setJumpPressed(false);
        }

        // reseteamos el contador de salto
        if (player.body.getLinearVelocity().y == 0 && colision) {
            player.jumpCounter = 0;
            colision = false;
        }

        if (player.getOnLadder() && joystick.getDirection().y > 0.5f) {
            player.body.setLinearVelocity(0, player.velY = 0.5f);
        } else if (player.getOnLadder() && joystick.getDirection().y < -0.5f) {
            player.body.setLinearVelocity(0, player.velY = -0.5f);
        } else if (player.getOnLadder()) {
            player.body.setLinearVelocity(0, 0);
        }

        player.body.setLinearVelocity(joystick.getDirection().x * player.speed,
                Math.min(player.body.getLinearVelocity().y, 15));
    }

    /**
     * Maneja la entrada en dispositivos de escritorio.
     * 
     * @param dt El tiempo delta.
     */
    private void handleDesktopInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            player.velX = 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.velX = -1f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.jumpCounter < 2) {
            game.playSound(jump);
            player.body.setLinearVelocity(0, 0);
            player.body.applyLinearImpulse(new Vector2(0, 2.5f), player.body.getWorldCenter(), true);
            player.jumpCounter++;
        }
        // reseteamos el contador de salto
        if (player.body.getLinearVelocity().y == 0 && colision) {
            player.jumpCounter = 0;
            colision = false;
        }

        if (player.getOnLadder() && Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.body.setLinearVelocity(0, player.velY = 0.5f);
        } else if (player.getOnLadder() && Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.body.setLinearVelocity(0, player.velY = -0.5f);
        } else if (player.getOnLadder()) {
            player.body.setLinearVelocity(0, 0);
            player.velY = 0;
        }

        player.body.setLinearVelocity(player.velX * player.speed,
                Math.min(player.body.getLinearVelocity().y, 15));
    }

    /**
     * Actualiza el estado del juego, incluyendo la física, la posición del jugador
     * y los objetos.
     * 
     * @param dt El tiempo delta para la actualización.
     */
    public void update(float dt) {
        // Asegura que las físicas se actualicen con una tasa fija,
        // sin importar la tasa de refresco de la pantalla.
        accumulator += dt;
        while (accumulator >= timeStep) {
            // Actualiza el mundo con un paso de tiempo fijo
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }

        handleInput(dt);

        // Control de gravedad en función del jugador
        if (player.getOnLadder()) {
            world.setGravity(new Vector2(0, 0));
        } else {
            world.setGravity(new Vector2(0, -9.8f));
        }

        // Actualización de sonidos de pasos
        updateSounds(dt);

        // Actualización de los objetos del juego
        player.update(dt);
        hud.updateHud(newLife, cherriesCollected, gemsCollected);

        // Manejo de la sala secreta
        handleSecretRoom();

        // Actualización de enemigos y objetos
        for (Enemy enemy : creator.getZarigueyas()) {
            enemy.update(dt);
        }
        for (Cherry cherry : creator.getCherries()) {
            cherry.update(dt);
        }
        for (Gem gem : creator.getGems()) {
            gem.update(dt);
        }

        // Actualización de la cámara
        if (!player.isDead()) {
            updateCameraPosition();
        }
        gamecam.update();
        renderer.setView(gamecam);
    }

    /**
     * Actualiza los sonidos de los pasos del jugador.
     * 
     * @param dt El tiempo delta.
     */
    private void updateSounds(float dt) {
        if ((player.body.getLinearVelocity().x != 0 && player.body.getLinearVelocity().y == 0)) {
            stepTimer += dt;
            if (stepTimer >= stepInterval) {
                stepTimer = 0;
                game.playSound(run);
            }
        } else {
            stepTimer = 0;
        }
    }

    /**
     * Maneja la visibilidad de la sala secreta en el mapa.
     */
    private void handleSecretRoom() {
        if (player.isInsideSecretRoom()) {
            toggleSecretRoom(true);
        } else {
            toggleSecretRoom(false);
        }
    }

    /**
     * Actualiza la posición de la cámara para que siga al jugador.
     */
    private void updateCameraPosition() {
        float minX = gamePort.getWorldWidth() / 2;
        float maxX = mapWidthInUnits - gamePort.getWorldWidth() / 2;

        if (player.body.getPosition().x < minX) {
            gamecam.position.x = minX;
        } else if (player.body.getPosition().x > maxX) {
            gamecam.position.x = maxX;
        } else {
            gamecam.position.x = player.body.getPosition().x;
        }

        float minY = gamePort.getWorldHeight() / 2;
        float maxY = mapHeightInUnits - gamePort.getWorldHeight() / 2;

        if (player.body.getPosition().y < minY) {
            gamecam.position.y = minY;
        } else if (player.body.getPosition().y > maxY) {
            gamecam.position.y = maxY;
        } else {
            gamecam.position.y = player.body.getPosition().y;
        }
    }

    /**
     * Finaliza el juego si el jugador está muerto o ha terminado el nivel.
     */
    public void finalGame() {
        if (player.isDead() && player.getStateTimer() > 3) {
            game.setScreen(new GameOverScreen(game, nivelActual));
            dispose();
        }
        if (player.isEndGame()) {
            game.setScreen(new FinalLevelScreen(nivelActual, game, this));
            dispose();
        }
    }

    private int[] pinchos = { 1 };

    /**
     * Renderiza todos los elementos en pantalla, incluyendo los gráficos, la física
     * y el HUD.
     * 
     * @param delta El tiempo delta para la renderización.
     */
    @Override
    public void render(float delta) {
        // lo primero que debe hacer es actualizarse
        update(delta);

        // Borra la pantalla y la deja negra
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // renderizador del juego
        renderer.render();
        // renderizamos el Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        // Dibuja los elementos del juego
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getZarigueyas()) {
            if (enemy.isActive()) {
                enemy.draw(game.batch);
            }
        }
        for (Cherry cherry : creator.getCherries()) {
            cherry.draw(game.batch);
        }
        for (Gem gem : creator.getGems()) {
            if (gem.isActive()) {
                gem.draw(game.batch);
            }
        }
        game.batch.end();
        renderer.render(pinchos);

        // Configura el batch para centrar la camara del HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Dibuja el joystick en Android
        if (Gdx.app.getType() == ApplicationType.Android)
            joystick.render();

        finalGame();
    }

    /**
     * Cambia la visibilidad de la sala secreta.
     * 
     * @param show Si debe mostrar la sala secreta o no.
     */
    private void toggleSecretRoom(boolean show) {
        MapLayer decoracionLayer = map.getLayers().get("SecretDecos");
        MapLayer decoracionLayer2 = map.getLayers().get("SecretDecos2");
        MapLayer habitacionLayer = map.getLayers().get("SalaSecreta");

        if (habitacionLayer != null) {
            if (decoracionLayer != null && habitacionLayer != null) {
                decoracionLayer.setVisible(show);
                if (decoracionLayer2 != null) {
                    decoracionLayer2.setVisible(show);
                }
                habitacionLayer.setVisible(show);
            }

            if (show) {
                for (Zarigueya enemy : creator.getZarigueyas()) {
                    if (enemy.isInSecretRoom()) {
                        enemy.setActive(show);
                    }
                }
                for (Gem gem : creator.getGems()) {
                    if (gem.isInSecretRoom()) {
                        gem.setActive(show);
                    }
                }
            }
        }
    }

    
    // --------- Actualizar HUD--------------
     /**
     * Incrementa la cantidad de cerezas recolectadas.
     */
    public void addCherry() {
        cherriesCollected++;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

    /**
     * Incrementa la cantidad de gemas recolectadas.
     */
    public void addGem() {
        gemsCollected++;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

     /**
     * Actualiza la cantidad de vidas restantes.
     * 
     * @param life Nuevas vidas restantes.
     */
    public void restLife(int life) {
        this.newLife = life;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

    /**
     * Redimensiona la pantalla.
     * 
     * @param width  Nuevo ancho de la pantalla.
     * @param height Nuevo alto de la pantalla.
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        joystick.resize(width, height);
    }

    /**
     * Libera todos los recursos utilizados por esta pantalla.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    // Métodos no utilizados, pero necesarios por la interfaz Screen
    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}
