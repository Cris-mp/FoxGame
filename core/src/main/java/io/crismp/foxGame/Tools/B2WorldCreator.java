package io.crismp.foxGame.Tools;

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
import io.crismp.foxGame.Sprites.Escalera;
import io.crismp.foxGame.Sprites.Zarigueya;
import io.crismp.foxGame.Sprites.Zarzas;
import io.crismp.foxGame.screens.PlayScreen;

public class B2WorldCreator {
    World world;

    ArrayList<RectangleMapObject> limites;
    ArrayList<Zarigueya> zarigueyas;
    public B2WorldCreator(PlayScreen screen) {

        world =screen.getWorld();
        TiledMap map = screen.getMap();
        
      //suelos
        for (MapObject object : map.getLayers().get("suelos").getObjects().getByType(RectangleMapObject.class)) {
            define(object,FoxGame.GROUND_BIT);
        }
        for (MapObject object : map.getLayers().get("paredes").getObjects().getByType(RectangleMapObject.class)) {
            define(object,FoxGame.WALL_BIT);
        }
        for (MapObject object : map.getLayers().get("techos").getObjects().getByType(RectangleMapObject.class)) {
            define(object,FoxGame.FLOOR_BIT);
        }
        for (MapObject object : map.getLayers().get("obstaculos").getObjects().getByType(RectangleMapObject.class)) {
            define(object,FoxGame.OBSTACLE_BIT);
        }

        for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Escalera(screen, rectangle);
        }

        for (MapObject object : map.getLayers().get("zarzas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Zarzas(screen, rectangle);
        }
        zarigueyas=new ArrayList<>();
        for (MapObject object : map.getLayers().get("zarigueya").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            zarigueyas.add(new Zarigueya(screen,rectangle));
        }
    }
    
    public ArrayList<Zarigueya> getZarigueyas(){
        return zarigueyas;
    }

    private void define(Object object,short mask){
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            // Definimos el cuerpo
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            // Determinamos si el objeto es estatico o dinamico
            bdef.type = BodyDef.BodyType.StaticBody;
            // Posicionamos el cuerpo
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / FoxGame.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / FoxGame.PPM);
            // Se crea el cuerpo en el mundo
            Body body = world.createBody(bdef);
            // damos tamaño a la forma
            shape.setAsBox((rectangle.getWidth() / 2) / FoxGame.PPM, (rectangle.getHeight() / 2) / FoxGame.PPM);
            // definimos la forma
            fdef.shape = shape;
            fdef.filter.categoryBits=mask;
            fdef.friction = 0;
            // la añadimos al cuerpo
            body.createFixture(fdef);
    }

}
