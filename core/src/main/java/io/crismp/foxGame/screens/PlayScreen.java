package io.crismp.foxGame.screens;

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
import io.crismp.foxGame.Tools.B2WorldCreator;
import io.crismp.foxGame.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private FoxGame game;
    private OrthographicCamera gamecam;

    //
    private Viewport gamePort;
    private Hud hud;

    // Variables de creacion de mapa y situacion de camara
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Creacion de mundo (Fisicas y cuerpos)
    private World world;
    private Box2DDebugRenderer b2dr;

    Foxy player;

    public PlayScreen(FoxGame game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        // mantiene el ratio de aspecto virtual a pesar de la pantalla
        gamePort = new FitViewport(FoxGame.V_WIDTH / FoxGame.PPM, FoxGame.V_HEIGHT / FoxGame.PPM, gamecam);

        // Crea los marcadores de fase y vista //TODO: Ampliar en futuro
        hud = new Hud(game.batch);

        // Carga nuestro mapa y configurar nuestro renderizador de mapas
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/Fase1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / FoxGame.PPM);

        // Situa la camara para que se centre correctamente el el punto 0
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // fisicas (world) y los cuerpos (brd2)
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // Pasamos al creador de mundo el mundo y el mapa
        new B2WorldCreator(world, map);

        // creamos a foxy
        player = new Foxy(world, map);

        world.setContactListener(new WorldContactListener());

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        player.velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.velX = 30f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.velX = -30f * dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.jumpCounter < 2) {// TODO:aqui no uso el dt porque no
                                                                                     // se como
            float force = player.body.getMass() * 2.5f;

            player.body.setLinearVelocity(player.body.getLinearVelocity().x, 0);
            player.body.applyLinearImpulse(new Vector2(0, force), player.body.getPosition(), true);
            player.jumpCounter++;
        }
        // reseteamos el contador de salto
        if (player.body.getLinearVelocity().y == 0) {
            player.jumpCounter = 0;
        }
        System.out.println(player.getOnLadder());
        if (player.getOnLadder() && Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.body.setLinearVelocity(0, player.velY = 30 * dt);
        }

        player.body.setLinearVelocity(player.velX * player.speed,
                player.body.getLinearVelocity().y < 15 ? player.body.getLinearVelocity().y : 15);

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        player.update(dt);

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
        // actualiza a las nuevas coordenadas
        gamecam.update();
        // llama al rederer para que se muestre solo el trozo que queremos ver del mundo
        renderer.setView(gamecam);
    }

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

        game.batch.end();

        // Configura el batch para centrar la camara del HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
    }

}
