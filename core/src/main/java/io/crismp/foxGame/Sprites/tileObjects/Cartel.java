package io.crismp.foxGame.sprites.tileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

/**
 * Representa un cartel interactivo en el mapa que puede tener un identificador
 * único. Hereda de InteractiveTiledObject para manejar la física y la detección
 * de colisiones.
 */
public class Cartel extends InteractiveTiledObject {

	/**
	 * Constructor para crear un objeto Cartel interactivo.
	 *
	 * @param screen Pantalla del juego donde se encuentra el objeto
	 * @param bounds Límites del objeto, usados para crear su cuerpo en el mundo
	 *               físico
	 * @param cartel Objeto que contiene las propiedades del cartel, como su ID
	 */
	public Cartel(PlayScreen screen, Rectangle bounds, MapObject cartel) {
		super(screen, bounds);
		setAsSensor(true); // Hace que el objeto sea un sensor y no interactúe físicamente
		fixture.setUserData(this);
		setCategoryFilter(FoxGame.CARTEL_BIT);

		// Asignamos un ID único al cartel, que se puede usar más adelante en la lógica
		// del juego
		fixture.setUserData(cartel.getProperties().get("id", Integer.class));
	}

}