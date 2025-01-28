package io.crismp.foxGame.Sprites.tileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Screens.PlayScreen;

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
