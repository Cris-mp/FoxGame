package io.crismp.foxGame.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.Sprites.Escalera;
import io.crismp.foxGame.Sprites.Suelo;
import io.crismp.foxGame.Sprites.Zarzas;

public class B2WorldCreator {

	public B2WorldCreator(World world, TiledMap map){
		        

        for (MapObject object : map.getLayers().get("suelos").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			new Suelo(world, map, rectangle);
        }
        
        for (MapObject object : map.getLayers().get("escaleras").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            
			new Escalera(world, map, rectangle);
        }
        
        for (MapObject object : map.getLayers().get("zarzas").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            new Zarzas(world, map, rectangle);
        }
	}
	
}
