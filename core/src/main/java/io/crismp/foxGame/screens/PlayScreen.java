package io.crismp.foxGame.Screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Scenes.Hud;
import io.crismp.foxGame.Sprites.Foxy;
import io.crismp.foxGame.Sprites.enemies.Enemy;
import io.crismp.foxGame.Sprites.items.Cherry;
import io.crismp.foxGame.Sprites.items.Gem;
import io.crismp.foxGame.Tools.B2WorldCreator;
import io.crismp.foxGame.Tools.VirtualJoystick;
import io.crismp.foxGame.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private FoxGame game;
    private OrthographicCamera gamecam;

    // HUD
    private Viewport gamePort;
    private Hud hud;
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

    VirtualJoystick joystick;
    float accumulator;
    float timeStep;

    // Parallax
    // private ParallaxLayer backgroundLayer1;
    // private ParallaxLayer backgroundLayer2;
    // private Texture bgTexture1;
    // private Texture bgTexture2;

    public PlayScreen(FoxGame game) {
        accumulator = 0f;
        timeStep = 1 / 60f;
        cherriesCollected = 0;
        gemsCollected = 0;
        newLife = 6;
        this.game = game;
        this.joystick = new VirtualJoystick(0, 0, 2, 1);
        gamecam = new OrthographicCamera();

        // mantiene el ratio de aspecto virtual a pesar de la pantalla
        gamePort = new FitViewport(FoxGame.V_WIDTH / FoxGame.PPM, FoxGame.V_HEIGHT / FoxGame.PPM, gamecam);

        // Crea los marcadores de fase y vista
        hud = new Hud(game.batch);

        // Carga nuestro mapa y configurar nuestro renderizador de mapas
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/Fase1.tmx");
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

        world.setContactListener(new WorldContactListener());

        // // Cargar texturas del fondo
        // bgTexture1 = new Texture("maps/back.png");
        // bgTexture2 = new Texture("maps/middle.png");

        // // Crear capas de parallax
        // backgroundLayer1 = new ParallaxLayer(bgTexture1, 0.2f, true, false, 0); //
        // Capa más lejana
        // backgroundLayer2 = new ParallaxLayer(bgTexture2, 0.5f, true, false, 260); //
        // Capa intermedia más abajo
        // backgroundLayer1.setCamera(new OrthographicCamera());
        // backgroundLayer2.setCamera(new OrthographicCamera());
    }

    public int getCherriesCollected() {
        return cherriesCollected;
    }

    public int getGemsCollected() {
        return gemsCollected;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (player.currenState != Foxy.State.DEAD) {
            player.velX = 0;
            if (Gdx.app.getType() == ApplicationType.Android) {
                if (joystick.isJumpPressed() && player.jumpCounter < 2) {
                    player.body.applyLinearImpulse(new Vector2(0, 2.5f), player.body.getWorldCenter(), true);
                    player.jumpCounter++;
                    joystick.setJumpPressed(false);
                }

                // reseteamos el contador de salto
                if (player.body.getLinearVelocity().y == 0) {
                    player.jumpCounter = 0;
                }

                if (player.getOnLadder() && joystick.getDirection().y > 0) {

                    player.body.setLinearVelocity(0, player.velY = 1f);
                } else {
                    player.body.setLinearVelocity(joystick.getDirection().x * player.speed,

                            Math.min(player.body.getLinearVelocity().y, 15));
                }
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    player.velX = 1f;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    player.velX = -1f;
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.jumpCounter < 2) {
                    player.body.applyLinearImpulse(new Vector2(0, 2.5f), player.body.getWorldCenter(), true);
                    player.jumpCounter++;
                }
                // reseteamos el contador de salto
                if (player.body.getLinearVelocity().y == 0) {
                    player.jumpCounter = 0;
                }

                if (player.getOnLadder() && Gdx.input.isKeyPressed(Input.Keys.W)) {
                    player.body.setLinearVelocity(0, player.velY = 1f);
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
        // world.step(dt, 6, 2);
        handleInput(dt);

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
        if (player.currenState != Foxy.State.DEAD) {
            // Ajuste de posicion de la camara en el eje X
            if (player.body.getPosition().x > 5.20f) {
                gamecam.position.x = 5.20f;
            } else {
                if (player.body.getPosition().x < 2.1f) {
                    gamecam.position.x = 2.10f;
                } else {
                    gamecam.position.x = player.body.getPosition().x;
                }
            }
            // Ajuste de posicion de la camara en el eje Y
            if (player.body.getPosition().y < 1.05f) {
                gamecam.position.y = 1.05f;
            } else {
                if (player.body.getPosition().y < 6.1f) {
                    gamecam.position.y = player.body.getPosition().y;
                } else {
                    gamecam.position.y = 6.1f;
                }
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
        // backgroundLayer1.render(game.batch); // Dibujar capa más lejana
        // backgroundLayer2.render(game.batch); // Dibujar capa intermedia
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

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver() {
        if (player.currenState == Foxy.State.DEAD && player.getStateTimer() > 3) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        joystick.resize(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

        // bgTexture1.dispose();
        // bgTexture2.dispose();
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

}
