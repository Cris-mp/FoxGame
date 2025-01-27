//NOTE: PARALLAX: debo renderizar las capas del mapa por separado. Ver:https://libgdx.com/wiki/graphics/2d/tile-maps

package io.crismp.foxGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.crismp.foxGame.screens.PlayScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class FoxGame extends Game {

    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;

    public static final short GROUND_BIT=1;
    public static final short FLOOR_BIT=2;
    public static final short WALL_BIT=4;
    public static final short OBSTACLE_BIT=16;
    public static final short FOX_BIT=32;
    public static final short ENEMY_BIT=64;
    public static final short ENEMY_HEAD_BIT=124;
    public static final short LADDER_BIT=248;
    public static final short ZARZAS_BIT=496;
   
    public SpriteBatch batch;


    @Override
    public void create() {
        batch= new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

}
