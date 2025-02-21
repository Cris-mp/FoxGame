package io.crismp.foxGame.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import io.crismp.foxGame.sprites.items.Cherry;
import io.crismp.foxGame.sprites.items.Gem;
import io.crismp.foxGame.tools.B2WorldCreator;
import io.crismp.foxGame.tools.VirtualJoystick;
import io.crismp.foxGame.tools.WorldContactListener;

public class PlayScreen implements Screen {
    public FoxGame game;

    private OrthographicCamera gamecam;

    // HUD
    private Viewport gamePort;
    public Hud hud;
    private int cherriesCollected;
    private int gemsCollected;
    private int newLife;

    // Variables de creacion de mapa y situacion de camara
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Creacion de mundo (Fisicas y cuerpos)
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Foxy player;

    private VirtualJoystick joystick;
    private float accumulator;
    private float timeStep;
    public boolean colision;

    private float mapWidthInUnits, mapHeightInUnits;
    private Sound jump, run;

    private float stepTimer = 0f;
    private float stepInterval = 0.3f;

    int nivelActual;

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

    public PlayScreen(FoxGame game, int nivelActual) {
        this.game = game;
        this.nivelActual=nivelActual;
        this.joystick = new VirtualJoystick(0, 0, 2, 1);
        gamecam = new OrthographicCamera();
        // mantiene el ratio de aspecto virtual a pesar de la pantalla
        gamePort = new FitViewport(FoxGame.V_WIDTH / FoxGame.PPM, FoxGame.V_HEIGHT / FoxGame.PPM, gamecam);

        accumulator = 0f;
        timeStep = 1 / 60f;
        cherriesCollected = 0;
        gemsCollected = 0;
        newLife = 6;
        colision = false;

        // Crea los marcadores de fase y vista
        hud = new Hud(game.batch);

        // Carga nuestro mapa y configurar nuestro renderizador de mapas
        mapLoader = new TmxMapLoader();
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
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FoxGame.PPM);

        // Situa la camara para que se centre correctamente el el punto 0
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // fisicas (world) y los cuerpos (brd2)
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        // Pasamos al creador de mundo el mundo y el mapa
        creator = new B2WorldCreator(this);

        // creamos a foxy
        player = new Foxy(this);

        world.setContactListener(new WorldContactListener(this));

        // Obtenemos las propiedades del mapa
        MapProperties prop = map.getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tileSize = prop.get("tilewidth", Integer.class);

        // Calcular el tamaño total del mapa en unidades del mundo
        mapWidthInUnits = (mapWidth * tileSize) / FoxGame.PPM;
        mapHeightInUnits = (mapHeight * tileSize) / FoxGame.PPM;

        jump = AssetsManagerAudio.getSound("audio/sounds/player/jump.ogg");
        run = AssetsManagerAudio.getSound("audio/sounds/player/Step_rock.ogg");

    }

    public void handleInput(float dt) {

        if (player.currenState != Foxy.State.DEAD) {
            player.velX = 0;
            if (Gdx.app.getType() == ApplicationType.Android) {
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

            } else {
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
        }
    }

    public void update(float dt) {

        // Asegura que las físicas se actualicen con una tasa fija, sin importar la tasa
        // de refresco de la pantalla.
        accumulator += dt;
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2); // Actualiza el mundo con un paso de tiempo fijo
            accumulator -= timeStep;
        }

        handleInput(dt);
        if (player.getOnLadder()) {
            world.setGravity(new Vector2(0, 0)); // Desactiva la gravedad mientras está en la escalera
        } else {
            world.setGravity(new Vector2(0, -9.8f)); // Vuelve a la gravedad normal
        }

        // sonido de pasos
        if ((player.body.getLinearVelocity().x != 0 && player.body.getLinearVelocity().y == 0)) {
            stepTimer += dt;
            if (stepTimer >= stepInterval) {
                stepTimer = 0;
                game.playSound(run);
            }
        } else {
            stepTimer = 0; // Reiniciar cuando no se mueve
        }

        player.update(dt);
        hud.updateHud(newLife, cherriesCollected, gemsCollected);

        for (Enemy enemy : creator.getZarigueyas()) {
            enemy.update(dt);
        }
        for (Cherry cherry : creator.getCherries()) {
            cherry.update(dt);
        }
        for (Gem gem : creator.getGems()) {
            gem.update(dt);
        }
        if (!player.isDead()) {
            // Ajuste de posición de la cámara en el eje X
            float minX = gamePort.getWorldWidth() / 2;
            float maxX = mapWidthInUnits - gamePort.getWorldWidth() / 2;

            if (player.body.getPosition().x < minX) {
                gamecam.position.x = minX;
            } else if (player.body.getPosition().x > maxX) {
                gamecam.position.x = maxX;
            } else {
                gamecam.position.x = player.body.getPosition().x;
            }

            // Ajuste de posición de la cámara en el eje Y
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
        // actualiza a las nuevas coordenadas
        gamecam.update();
        // llama al rederer para que se muestre solo el trozo que queremos ver del mundo
        renderer.setView(gamecam);
    }

    private int[] pinchos = { 1 };

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

        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();

        player.draw(game.batch);

        for (Enemy enemy : creator.getZarigueyas()) {
            enemy.draw(game.batch);
        }
        for (Cherry cherry : creator.getCherries()) {
            cherry.draw(game.batch);
        }
        for (Gem gem : creator.getGems()) {
            gem.draw(game.batch);
        }
        game.batch.end();

        renderer.render(pinchos);
        // Configura el batch para centrar la camara del HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (Gdx.app.getType() == ApplicationType.Android)
            joystick.render();

        finalGame();
    }

    public void finalGame() {
        if (player.isDead() && player.getStateTimer() > 3) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if (player.isEndGame()) {
            game.setScreen(new FinalLevelScreen(nivelActual,game, this));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        joystick.resize(width, height);
    }

    // --------- Actualizar HUD--------------
    public void addCherry() {
        cherriesCollected++;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

    public void addGem() {
        gemsCollected++;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

    public void restLife(int life) {
        this.newLife = life;
        hud.updateHud(newLife, cherriesCollected, gemsCollected);
    }

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
