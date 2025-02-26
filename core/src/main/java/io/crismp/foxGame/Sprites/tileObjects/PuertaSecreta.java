package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

/**
 * Representa una puerta secreta interactiva que puede ser abierta o cerrada.
 * Hereda de InteractiveTiledObject para manejar la física y la detección de colisiones.
 */
public class PuertaSecreta extends InteractiveTiledObject {

    /**
     * Constructor para crear una puerta secreta interactiva en el mapa.
     *
     * @param screen Pantalla del juego donde se encuentra el objeto
     * @param bounds Límites del objeto, usados para crear su cuerpo en el mundo físico
     */
    public PuertaSecreta(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        setAsSensor(true);
        fixture.setUserData(this);
        setCategoryFilter(FoxGame.SECRET_DOOR_BIT);
    }
}