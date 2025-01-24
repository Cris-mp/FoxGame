package io.crismp.foxGame.Sprites;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.screens.PlayScreen;

public abstract class Enemy extends Sprite{
    protected World world;
    public TiledMap map;
    protected PlayScreen screen;
    public Body body;

    public Enemy(PlayScreen screen,float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        this.map = screen.getMap();

        defineEnenmy();
    }
    protected abstract void defineEnenmy();

}
