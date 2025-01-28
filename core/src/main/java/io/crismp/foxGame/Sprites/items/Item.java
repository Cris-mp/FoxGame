package io.crismp.foxGame.Sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Screens.PlayScreen;
import io.crismp.foxGame.Sprites.Foxy;

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    public Rectangle rect;

    public Item(PlayScreen screen, Rectangle rect){
        this.screen = screen;
        this.world = screen.getWorld();
        this.rect=rect;
        toDestroy = false;
        destroyed = false;

        setPosition(rect.getX(), rect.getY());
        setBounds(getX(), getY(), 16 / FoxGame.PPM, 16 / FoxGame.PPM);
        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Foxy foxy);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }

}
