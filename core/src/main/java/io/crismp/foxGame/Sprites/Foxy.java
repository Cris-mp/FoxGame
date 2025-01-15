package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;

public class Foxy extends Sprite{
    public World world;
    public Body body;
    public TiledMap map;

    public Foxy(World world,TiledMap map){
        this.world = world;
        this.map=map;
        defineFoxy();
    }

    public void defineFoxy(){
        BodyDef bdef = new BodyDef();

        for(MapObject object: map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            //Posicionamos el cuerpo
            bdef.position.set((rectangle.getX())/FoxGame.PPM, (rectangle.getY())/FoxGame.PPM);  
        };
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/FoxGame.PPM);

        fdef.shape=shape;
        body.createFixture(fdef);

    }

    
}
