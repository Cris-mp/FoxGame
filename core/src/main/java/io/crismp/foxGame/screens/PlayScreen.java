package io.crismp.foxGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Scenes.Hud;
import io.crismp.foxGame.Sprites.Foxy;

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

        // Crea los marcadores de fase y visa //TODO: Ampliar en futuro
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

        // creamos a foxy
        player = new Foxy(world, map);

        // --------Esto lo pondremos en sus clases despues--------------

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get("suelos").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            // Definimos el cuerpo
            bdef = new BodyDef();
            // Determinamos si el objeto es estatico o dinamico
            bdef.type = BodyDef.BodyType.StaticBody;
            // Posicionamos el cuerpo
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / FoxGame.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / FoxGame.PPM);
            // Se crea el cuerpo en el mundo
            body = world.createBody(bdef);
            // damos tamaño a la forma
            shape.setAsBox((rectangle.getWidth() / 2) / FoxGame.PPM, (rectangle.getHeight() / 2) / FoxGame.PPM);
            // definimos la forma
            fdef.shape = shape;
            fdef.friction = 0;
            // la añadimos al cuerpo
            body.createFixture(fdef);
        }
        ;

        for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            // Definimos el cuerpo
            bdef = new BodyDef();
            // Determinamos si el objeto es estatico o dinamico
            bdef.type = BodyDef.BodyType.StaticBody;
            // Posicionamos el cuerpo
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / FoxGame.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / FoxGame.PPM);
            // Se crea el cuerpo en el mundo
            body = world.createBody(bdef);
            // damos tamaño a la forma
            shape.setAsBox((rectangle.getWidth() / 2) / FoxGame.PPM, (rectangle.getHeight() / 2) / FoxGame.PPM);
            // definimos la forma
            fdef.shape = shape;
            // la añadimos al cuerpo
            body.createFixture(fdef);
        }
        ;

        for (MapObject object : map.getLayers().get("zarzas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            // Definimos el cuerpo
            bdef = new BodyDef();
            // Determinamos si el objeto es estatico o dinamico
            bdef.type = BodyDef.BodyType.StaticBody;
            // Posicionamos el cuerpo
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / FoxGame.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / FoxGame.PPM);
            // Se crea el cuerpo en el mundo
            body = world.createBody(bdef);
            // damos tamaño a la forma
            shape.setAsBox((rectangle.getWidth() / 2) / FoxGame.PPM, (rectangle.getHeight() / 2) / FoxGame.PPM);
            // definimos la forma
            fdef.shape = shape;
            // la añadimos al cuerpo
            body.createFixture(fdef);
        }
        ;

    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        //TODO: creo que el linear impilse no es el mas adecuado aqui,
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(-0.01f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.body.applyLinearImpulse(new Vector2(0, 4f), player.body.getPosition(), true);
        }

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        gamecam.position.x = player.body.getPosition().x;

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

    }

}
