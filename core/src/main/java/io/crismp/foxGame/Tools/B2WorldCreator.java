package io.crismp.foxGame.tools;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.enemies.Zarigueya;
import io.crismp.foxGame.sprites.items.Cherry;
import io.crismp.foxGame.sprites.items.Gem;
import io.crismp.foxGame.sprites.tileObjects.Cartel;
import io.crismp.foxGame.sprites.tileObjects.Escalera;
import io.crismp.foxGame.sprites.tileObjects.Pinchos;

/**
 * Clase encargada de generar y configurar los objetos del mundo físico (Box2D)
 * a partir de un mapa de TiledMap. Se encarga de crear los cuerpos físicos
 * correspondientes a los elementos del mapa, como suelos, paredes, enemigos,
 * obstáculos y coleccionables.
 */
public class B2WorldCreator {
    private World world;  // Referencia al mundo físico (Box2D)
    private TiledMap map; // Mapa del juego (TiledMap)

    private ArrayList<Zarigueya> zarigueyas; // Lista de enemigos zarigüeyas
    private ArrayList<Cherry> cherrys; // Lista de cerezas coleccionables
    private ArrayList<Gem> gems; // Lista de gemas coleccionables

    /**
     * Constructor de la clase B2WorldCreator.
     * Se encarga de leer las capas del mapa y generar los cuerpos físicos
     * correspondientes en el mundo de Box2D.
     *
     * @param screen La pantalla del juego que contiene el mundo físico y el mapa.
     */
    public B2WorldCreator(PlayScreen screen) {
        this.world = screen.getWorld();
        this.map = screen.getMap();

        // Crear colisiones para elementos del entorno
        createStaticBodies("suelos", FoxGame.GROUND_BIT);
        createStaticBodies("paredes", FoxGame.WALL_BIT);
        createStaticBodies("techos", FoxGame.FLOOR_BIT);
        createStaticBodies("obstaculos", FoxGame.OBSTACLE_BIT);
        createStaticBodies("puerta", FoxGame.END_GAME_BIT);
        createStaticBodies("rampas", FoxGame.RAMP_BIT);

        // Crear objetos interactivos
        createLadders(screen);
        createSpikes(screen);
        createSigns(screen);

        // Crear enemigos y coleccionables
        zarigueyas = createEnemies(screen);
        cherrys = createCherries(screen);
        gems = createGems(screen);
    }

    /**
     * Crea cuerpos estáticos en el mundo de Box2D para los objetos de una capa específica del mapa.
     *
     * @param layerName Nombre de la capa en el mapa.
     * @param mask      Máscara de bits que define la colisión del objeto.
     */
    private void createStaticBodies(String layerName, short mask) {
        if (map.getLayers().get(layerName) != null) {
            for (MapObject object : map.getLayers().get(layerName).getObjects().getByType(RectangleMapObject.class)) {
                defineStaticBody(object, mask);
            }
            // Manejar polígonos
            for (MapObject object : map.getLayers().get(layerName).getObjects().getByType(PolygonMapObject.class)) {
                definePolygonBody(object, mask);
            }
        }
    }

    /**
     * Crea las escaleras en el mundo del juego.
     *
     * @param screen Pantalla de juego.
     */
    private void createLadders(PlayScreen screen) {
        if (map.getLayers().get("escaleras") != null) {
            for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
                new Escalera(screen, ((RectangleMapObject) object).getRectangle());
            }
        }
    }

    /**
     * Crea los pinchos en el mundo del juego.
     *
     * @param screen Pantalla de juego.
     */
    private void createSpikes(PlayScreen screen) {
        if (map.getLayers().get("pinchos") != null) {
            for (MapObject object : map.getLayers().get("pinchos").getObjects().getByType(RectangleMapObject.class)) {
                new Pinchos(screen, ((RectangleMapObject) object).getRectangle());
            }
        }
    }

    /**
     * Crea los carteles en el mundo del juego.
     *
     * @param screen Pantalla de juego.
     */
    private void createSigns(PlayScreen screen) {
        if (map.getLayers().get("cartel") != null) {
            for (MapObject object : map.getLayers().get("cartel").getObjects().getByType(RectangleMapObject.class)) {
                new Cartel(screen, ((RectangleMapObject) object).getRectangle(), object);
            }
        }
    }

    /**
     * Crea la lista de enemigos zarigüeyas en el juego.
     *
     * @param screen Pantalla de juego.
     * @return Lista de enemigos zarigüeyas.
     */
    private ArrayList<Zarigueya> createEnemies(PlayScreen screen) {
        ArrayList<Zarigueya> enemies = new ArrayList<>();
        if (map.getLayers().get("zarigueya") != null) {
            for (MapObject object : map.getLayers().get("zarigueya").getObjects().getByType(RectangleMapObject.class)) {
                enemies.add(new Zarigueya(screen, ((RectangleMapObject) object).getRectangle()));
            }
        }
        return enemies;
    }

    /**
     * Crea la lista de cerezas coleccionables en el juego.
     *
     * @param screen Pantalla de juego.
     * @return Lista de cerezas coleccionables.
     */
    private ArrayList<Cherry> createCherries(PlayScreen screen) {
        ArrayList<Cherry> items = new ArrayList<>();
        if (map.getLayers().get("cherries") != null) {
            for (MapObject object : map.getLayers().get("cherries").getObjects().getByType(RectangleMapObject.class)) {
                items.add(new Cherry(screen, ((RectangleMapObject) object).getRectangle()));
            }
        }
        return items;
    }

    /**
     * Crea la lista de gemas coleccionables en el juego.
     *
     * @param screen Pantalla de juego.
     * @return Lista de gemas coleccionables.
     */
    private ArrayList<Gem> createGems(PlayScreen screen) {
        ArrayList<Gem> items = new ArrayList<>();
        if (map.getLayers().get("gems") != null) {
            for (MapObject object : map.getLayers().get("gems").getObjects().getByType(RectangleMapObject.class)) {
                items.add(new Gem(screen, ((RectangleMapObject) object).getRectangle()));
            }
        }
        return items;
    }

    /**
     * Obtiene la lista de enemigos zarigüeyas.
     *
     * @return Lista de enemigos zarigüeyas.
     */
    public ArrayList<Zarigueya> getZarigueyas() {
        return zarigueyas;
    }

    /**
     * Obtiene la lista de cerezas coleccionables.
     *
     * @return Lista de cerezas coleccionables.
     */
    public ArrayList<Cherry> getCherries() {
        return cherrys;
    }

    /**
     * Obtiene la lista de gemas coleccionables.
     *
     * @return Lista de gemas coleccionables.
     */
    public ArrayList<Gem> getGems() {
        return gems;
    }

    /**
     * Define un cuerpo estático en el mundo de Box2D para un objeto del mapa.
     * Los cuerpos estáticos no se mueven y sirven como colisión con el entorno.
     *
     * @param object Objeto del mapa a convertir en un cuerpo estático.
     * @param mask   Máscara de colisión para diferenciar el tipo de objeto.
     */
    private void defineStaticBody(MapObject object, short mask) {
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / FoxGame.PPM,
                          (rectangle.getY() + rectangle.getHeight() / 2) / FoxGame.PPM);

        Body body = world.createBody(bdef);
        shape.setAsBox((rectangle.getWidth() / 2) / FoxGame.PPM, (rectangle.getHeight() / 2) / FoxGame.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = mask;
        fdef.friction = 0;
        body.createFixture(fdef);
    }

    private void definePolygonBody(MapObject object, short mask) {
    PolygonMapObject polygonObject = (PolygonMapObject) object;
    Polygon polygon = polygonObject.getPolygon();
    
    // Definir cuerpo
    BodyDef bdef = new BodyDef();
    bdef.type = BodyDef.BodyType.StaticBody;
    bdef.position.set(polygon.getX() / FoxGame.PPM, polygon.getY() / FoxGame.PPM);
    
    Body body = world.createBody(bdef);

    // Crear la forma del polígono
    FixtureDef fdef = new FixtureDef();
    PolygonShape shape = new PolygonShape();

    float[] vertices = polygon.getVertices();
    float[] worldVertices = new float[vertices.length];

    // Convertir las coordenadas a Box2D (dividir por PPM)
    for (int i = 0; i < vertices.length; i++) {
        worldVertices[i] = vertices[i] / FoxGame.PPM;
    }
    shape.set(worldVertices);

    // Configurar la fixture
    fdef.shape = shape;
    fdef.filter.categoryBits = mask;
    fdef.friction = 3;
    body.createFixture(fdef);

    // Liberar la memoria de la forma
    shape.dispose();
}
}