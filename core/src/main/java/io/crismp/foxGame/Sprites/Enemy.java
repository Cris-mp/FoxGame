package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.screens.PlayScreen;

public class Enemy extends Sprite{
    World world;
    PlayScreen screen;
    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);

    }

}
