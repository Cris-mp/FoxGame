package io.crismp.foxGame.Sprites.tileObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Screens.PlayScreen;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;

public abstract class InteractiveTiledObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTiledObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        // Definimos el cuerpo
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // Determinamos si el objeto es estatico o dinamico
        bdef.type = BodyDef.BodyType.StaticBody;
        // Posicionamos el cuerpo
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / FoxGame.PPM,
                (bounds.getY() + bounds.getHeight() / 2) / FoxGame.PPM);

        // Se crea el cuerpo en el mundo
        body = world.createBody(bdef);

        // damos tamaño a la forma
        shape.setAsBox((bounds.getWidth() / 2) / FoxGame.PPM, (bounds.getHeight() / 2) / FoxGame.PPM);
        // definimos la forma
        fdef.shape = shape;
        fdef.friction = 0;
        // la añadimos al cuerpo y lo guardamos
        fixture = body.createFixture(fdef);
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

}
