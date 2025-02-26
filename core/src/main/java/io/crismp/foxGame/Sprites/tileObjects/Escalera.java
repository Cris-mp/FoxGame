package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

/**
 * Representa una escalera interactiva en el mapa donde el jugador puede subir.
 * Hereda de InteractiveTiledObject para manejar la física y la detección de
 * colisiones.
 */
public class Escalera extends InteractiveTiledObject {

	/**
	 * Constructor para crear una escalera interactiva en el mapa.
	 *
	 * @param screen Pantalla del juego donde se encuentra el objeto
	 * @param bounds Límites del objeto, usados para crear su cuerpo en el mundo
	 *               físico
	 */
	public Escalera(PlayScreen screen, Rectangle bounds) {
		super(screen, bounds);
		setAsSensor(true); // La escalera se comporta como un sensor
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.LADDER_BIT); // Filtro de la escalera
	}
}
