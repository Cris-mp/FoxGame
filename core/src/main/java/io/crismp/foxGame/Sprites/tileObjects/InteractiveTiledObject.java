package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

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

    /**
     * Clase base para todos los objetos interactivos en el mapa que utilizan la
     * física del mundo.
     * Maneja la creación y configuración del cuerpo y el fixture de los objetos en
     * el mundo.
     */
    public InteractiveTiledObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        //  Crear el cuerpo del objeto
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

        // Definir el tamaño del objeto
        shape.setAsBox((bounds.getWidth() / 2) / FoxGame.PPM, (bounds.getHeight() / 2) / FoxGame.PPM);

        // Configurar el fixture
        fdef.shape = shape;
        fdef.friction = 0;

        // Añadir el fixture al cuerpo
        fixture = body.createFixture(fdef);
    }

    /**
     * Establece el filtro de categoría para el fixture.
     * 
     * @param filterBit El filtro de categoría que define las interacciones del objeto
     */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    /**
     * Configura si el objeto será un sensor (sin interacciones físicas).
     * Este método se puede sobreescribir en las clases hijas para habilitar el sensor si es necesario.
     *
     * @param isSensor Si es true, el objeto no interfiere físicamente, solo detecta colisiones.
     */
    protected void setAsSensor(boolean isSensor) {
        fixture.setSensor(isSensor);
    }

}
