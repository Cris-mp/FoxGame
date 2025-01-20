package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Zarzas extends InteractiveTiledObject{
	public Zarzas(PlayScreen screen, Rectangle bounds){
		super(screen,bounds);
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.ZARZAS_BIT);
	}

	@Override
	public void onHeadHit() {
	Gdx.app.log("zarza", "Colision");
	}
}
