package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Pinchos extends InteractiveTiledObject{
	public Pinchos(PlayScreen screen, Rectangle bounds){
		super(screen,bounds);
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.SPIKES_BIT);
	}


}
