package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Escalera extends InteractiveTiledObject {

	public Escalera(PlayScreen screen, Rectangle bounds){
		super(screen,bounds);
		fixture.setSensor(true);
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.LADDER_BIT);
	}
}
