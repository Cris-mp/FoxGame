package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Suelo extends InteractiveTiledObject {
	public Suelo(World world, TiledMap map, Rectangle bounds){
		super(world,map,bounds);
		
	}
}
