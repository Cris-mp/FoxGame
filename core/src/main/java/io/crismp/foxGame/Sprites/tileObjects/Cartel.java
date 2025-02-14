package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Cartel extends InteractiveTiledObject {

	public Cartel(PlayScreen screen, Rectangle bounds, MapObject cartel){
		super(screen,bounds);
		fixture.setSensor(true);
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.CARTEL_BIT);
       fixture.setUserData(cartel.getProperties().get("id", Integer.class));
	}
}