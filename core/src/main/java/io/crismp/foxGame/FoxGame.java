//NOTE: PARALLAX: debo renderizar las capas del mapa por separado. Ver:https://libgdx.com/wiki/graphics/2d/tile-maps

package io.crismp.foxGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.crismp.foxGame.Screens.MainMenuScreen;
import io.crismp.foxGame.Tools.AssetsManager;

public class FoxGame extends Game {

    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;

    public static final short NOTHING_BIT = 0; // 0000000000000000
    public static final short GROUND_BIT = 1; // 0000000000000001
    public static final short FLOOR_BIT = 2; // 0000000000000010
    public static final short WALL_BIT = 4; // 0000000000000100
    public static final short OBSTACLE_BIT = 8; // 0000000000001000

    public static final short FOX_BIT = 16; // 0000000000010000
    public static final short FOX_HEAD_BIT = 32; // 0000000000100000

    public static final short ENEMY_BIT = 64; // 0000000001000000
    public static final short ENEMY_HEAD_BIT = 128; // 0000000010000000

    public static final short LADDER_BIT = 256; // 0000000100000000
    public static final short PINCHOS_BIT = 512; // 0000001000000000
    public static final short ITEM_BIT = 1024; // 0000010000000000
    public static final short END_GAME_BIT = 2048; // 0000100000000000

    public SpriteBatch batch;
    public boolean isVibrationOn = true;

    @Override
    public void create() {
        batch = new SpriteBatch();
        AssetsManager.load();
        AssetsManager.finishLoading();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public void dispose() {
        super.render();
        AssetsManager.dispose();// manager de asert
    }

}
