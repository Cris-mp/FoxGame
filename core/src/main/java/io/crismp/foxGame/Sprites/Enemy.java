package io.crismp.foxGame.Sprites;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.screens.PlayScreen;

public abstract class Enemy extends Sprite{
    protected World world;
    public TiledMap map;
    protected PlayScreen screen;
    public Body body;
    public Vector2 velocity;
    public Rectangle rect;

    public Enemy(PlayScreen screen,Rectangle rect){
        this.screen = screen;
        this.rect =rect;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        defineEnenmy();
        velocity=new Vector2(0.5f,0);
    }
    protected abstract void defineEnenmy();
    public abstract void update(float dt);
    public abstract void hitOnHead();
    public void reverseVelocity(boolean x, boolean y){
        if(x)
        velocity.x=-velocity.x;
        if(y)
        velocity.y=-velocity.y;
    }

}
