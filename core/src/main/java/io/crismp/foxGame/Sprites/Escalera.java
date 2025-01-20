package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Escalera extends InteractiveTiledObject {

	public Escalera(PlayScreen screen, Rectangle bounds){
		super(screen,bounds);
		fixture.setSensor(true);
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.LADDER_BIT);
	}
	@Override
	public void onHeadHit() {
		Gdx.app.log("Escalera", "Colision");
	}
}
