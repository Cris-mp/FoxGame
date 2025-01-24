package io.crismp.foxGame.Tools;

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
import io.crismp.foxGame.Sprites.Zarzas;
import io.crismp.foxGame.screens.PlayScreen;

public class B2WorldCreator {

    public B2WorldCreator(PlayScreen screen) {

        World world =screen.getWorld();
        TiledMap map = screen.getMap();

        // Los suelos no son objetos interactivos asi que se quedan aqui
        for (MapObject object : map.getLayers().get("suelos").getObjects().getByType(RectangleMapObject.class)) {
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
            fdef.filter.categoryBits=FoxGame.OBJECT_BIT;//para diferenciar que choca contra objetos
            fdef.friction = 0;
            // la añadimos al cuerpo
            body.createFixture(fdef);
        }

        for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Escalera(screen, rectangle);
        }

        for (MapObject object : map.getLayers().get("zarzas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Zarzas(screen, rectangle);
        }
    }

}
