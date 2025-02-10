package io.crismp.foxGame.tools;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import io.crismp.foxGame.sprites.tileObjects.Escalera;
import io.crismp.foxGame.sprites.tileObjects.Pinchos;

/**
 * Crea y configura los objetos del mundo del juego a partir de un mapa de
 * objetos (TiledMap). Cada objeto del mapa se asocia con un cuerpo físico en el
 * mundo de Box2D, permitiendo su interacción física.
 * 
 * El constructor de esta clase itera sobre las capas del mapa que contienen los
 * objetos del juego, como suelos, paredes, techos, obstáculos, escaleras,
 * pinchos, zarigueyas, cerezas, gemas y la puerta de fin de nivel. Para cada
 * uno de estos objetos, se les asigna un cuerpo estático o dinámico,
 * según el caso, en el mundo físico.
 */
public class B2WorldCreator {
    World world;
    TiledMap map;

    ArrayList<Zarigueya> zarigueyas;
    ArrayList<Cherry> cherrys;
    ArrayList<Gem> gems;

    /**
     * Este método itera sobre las capas del mapa de objetos y crea los objetos
     * correspondientes en el mundo físico (Box2D), asignándoles cuerpos físicos
     * estáticos o dinámicos.
     * 
     * @param screen El objeto PlayScreen que gestiona la pantalla del juego,
     *               proporcionando acceso al mundo físico y al mapa de objetos.
     */
    public B2WorldCreator(PlayScreen screen) {

        world = screen.getWorld();
        map = screen.getMap();

        for (MapObject object : map.getLayers().get("suelos").getObjects().getByType(RectangleMapObject.class)) {
            define(object, FoxGame.GROUND_BIT);
        }

        for (MapObject object : map.getLayers().get("paredes").getObjects().getByType(RectangleMapObject.class)) {
            define(object, FoxGame.WALL_BIT);
        }

        for (MapObject object : map.getLayers().get("techos").getObjects().getByType(RectangleMapObject.class)) {
            define(object, FoxGame.FLOOR_BIT);
        }

        for (MapObject object : map.getLayers().get("obstaculos").getObjects().getByType(RectangleMapObject.class)) {
            define(object, FoxGame.OBSTACLE_BIT);
        }

        for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Escalera(screen, rectangle);
        }

        for (MapObject object : map.getLayers().get("pinchos").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Pinchos(screen, rectangle);
        }
        zarigueyas = new ArrayList<>();
        for (MapObject object : map.getLayers().get("zarigueya").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            zarigueyas.add(new Zarigueya(screen, rectangle));
        }

        cherrys = new ArrayList<>();
        for (MapObject object : map.getLayers().get("cherries").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            cherrys.add(new Cherry(screen, rectangle));
        }

        gems = new ArrayList<>();
        for (MapObject object : map.getLayers().get("gems").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            gems.add(new Gem(screen, rectangle));
        }

        for (MapObject object : map.getLayers().get("puerta").getObjects().getByType(RectangleMapObject.class)) {
            define(object, FoxGame.END_GAME_BIT);
        }
    }

    /**
     * Devuelve la lista de zarigueyas presentes en el juego.
     * 
     * @return La lista de zarigueyas.
     */
    public ArrayList<Zarigueya> getZarigueyas() {
        return zarigueyas;
    }

    /**
     * Devuelve la lista de cerezas presentes en el juego.
     * 
     * @return La lista de cerezas.
     */
    public ArrayList<Cherry> getCherries() {
        return cherrys;
    }

    /**
     * Devuelve la lista de gemas presentes en el juego.
     * 
     * @return La lista de gemas.
     */
    public ArrayList<Gem> getGems() {
        return gems;
    }

    /**
     * Define un cuerpo estático en el mundo de Box2D para un objeto del mapa de
     * tipo RectangleMapObject. Este método crea un cuerpo estático (sin movimiento)
     * y lo coloca en la posición y tamaño del objeto en el mundo de acuerdo a las
     * coordenadas del mapa.
     * 
     * @param object El objeto tipo RectangleMapObject que se utilizará para definir
     *               el cuerpo en el mundo.
     * @param mask   La máscara de bits que identifica el tipo de objeto creado en
     *               el mundo físico.
     */
    private void define(Object object, short mask) {
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

}
