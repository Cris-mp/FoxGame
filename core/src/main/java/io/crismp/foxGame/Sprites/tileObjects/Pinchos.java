package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

/**
 * Representa una trampa de pinchos interactiva que puede dañar al jugador.
 * Hereda de InteractiveTiledObject para manejar la física y la detección de
 * colisiones.
 */
public class Pinchos extends InteractiveTiledObject {

	/**
	 * Constructor para crear una trampa de pinchos interactiva en el mapa.
	 *
	 * @param screen Pantalla del juego donde se encuentra el objeto
	 * @param bounds Límites del objeto, usados para crear su cuerpo en el mundo
	 *               físico
	 */
	public Pinchos(PlayScreen screen, Rectangle bounds) {
		super(screen, bounds);
		fixture.setUserData(this); // Asigna este objeto como usuario de los fixtures
		setCategoryFilter(FoxGame.SPIKES_BIT); // Filtro para los pinchos
	}
}
