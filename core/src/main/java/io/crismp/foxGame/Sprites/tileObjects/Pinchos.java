package io.crismp.foxGame.Sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Screens.PlayScreen;

public class Pinchos extends InteractiveTiledObject{
	public Pinchos(PlayScreen screen, Rectangle bounds){
		super(screen,bounds);
		fixture.setUserData(this);
		fixture.setSensor(true);
		setCategoryFilter(FoxGame.PINCHOS_BIT);
	}

	
}
