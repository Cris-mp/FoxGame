package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Zarzas extends InteractiveTiledObject{
	public Zarzas(World world, TiledMap map, Rectangle bounds){
		super(world,map,bounds);
		fixture.setUserData(this);
	}

	@Override
	public void onHeadHit() {
	Gdx.app.log("zarza", "Colision");
	}
}
