package io.crismp.foxGame.screens;

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
import io.crismp.foxGame.Sprites.Zarigueya;
import io.crismp.foxGame.Tools.B2WorldCreator;
import io.crismp.foxGame.Tools.VirtualJoystick;
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

    private Foxy player;
    private Zarigueya zarigueya;

    VirtualJoystick joystick;

    public PlayScreen(FoxGame game) {
        this.game = game;
        this.joystick = new VirtualJoystick(0, 0, 2, 1);
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
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        // Pasamos al creador de mundo el mundo y el mapa
        new B2WorldCreator(this);

        // creamos a foxy
        player = new Foxy(this);
        zarigueya=new Zarigueya(this, .32f,.32f);

        world.setContactListener(new WorldContactListener());

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {

        if (Gdx.app.getType() == ApplicationType.Android) {
            player.jumpCounter = 0;
            if (joystick.isJumpPressed() && player.jumpCounter < 2) {// TODO:aqui no uso el dt porque no se como
                float force;
                System.out.println(player.jumpCounter);
                joystick.setJumpPressed(false);
                if(dt>5){
                    force = player.body.getMass() * 2.5f;
                }else{
                    force = player.body.getMass() * 1.50f;
                }
                player.body.applyForceToCenter(0, 175f, true);
                player.jumpCounter++;
                System.out.println(player.jumpCounter);
            }
            // reseteamos el contador de salto
            if (player.body.getLinearVelocity().y == 0 && player.jumpCounter!=0) {
                System.out.println(player.jumpCounter);
                player.jumpCounter = 0;
            }

            if (player.getOnLadder() && joystick.getDirection().y > 0) {
                float force;
                if(dt>5){
                    force = player.body.getMass() * 2.5f;
                }else{
                    force = player.body.getMass() * 0.5f;
                }
                player.body.setLinearVelocity(0, player.velY = force);
            }
            float vel;
                if(dt>5){
                    vel = player.body.getMass() * 2.5f;
                }else{
                    vel = player.body.getMass() * 1.75f;
                }
            player.body.setLinearVelocity(joystick.getDirection().x/vel,
                    player.body.getLinearVelocity().y < 15 ? player.body.getLinearVelocity().y : 15);

        } else {
            player.velX = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                player.velX = 30f * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                player.velX = -30f * dt;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.jumpCounter < 2) {// TODO:aqui no uso el dt
                                                                                         // porque no
                                                                                         // se como
                float force = player.body.getMass() * 2.5f;

                player.body.setLinearVelocity(player.body.getLinearVelocity().x, 0);
                player.body.applyLinearImpulse(new Vector2(0, force), player.body.getWorldCenter(), true);
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

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        player.update(dt);
        //zarigueya.update(dt);

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
        //zarigueya.draw(game.batch);

        game.batch.end();

        // Configura el batch para centrar la camara del HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (Gdx.app.getType() == ApplicationType.Android)
            joystick.render();

            System.out.println(zarigueya.getX());
            System.out.println(player.getX());
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
    }

}
