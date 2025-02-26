package io.crismp.foxGame.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.screens.PlayScreen;

/**
 * Clase abstracta que representa a un enemigo en el juego.
 * Esta clase proporciona la estructura básica para crear enemigos en el juego.
 * Los enemigos se definen con una posición, un cuerpo físico, una velocidad y
 * un estado de activación.
 */
public abstract class Enemy extends Sprite {
    protected PlayScreen screen;
    protected World world;
    private boolean active;
    public TiledMap map;
    public Body body;
    public Vector2 velocity;
    public Rectangle rect;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Constructor de la clase `Enemy`.
     * Inicializa los atributos del enemigo utilizando los parámetros
     * proporcionados.
     * 
     * @param screen La pantalla de juego en la que se encuentra el enemigo.
     * @param rect   El rectángulo que define la posición y el tamaño del enemigo.
     */
    public Enemy(PlayScreen screen, Rectangle rect) {
        this.screen = screen;
        this.rect = rect;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.active = true;
        velocity = new Vector2(0.5f, 0);
        defineEnenmy();
    }

    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Este método define las propiedades del enemigo, como su cuerpo físico en el
     * mundo de Box2D.
     * Las subclases deben proporcionar su implementación específica.
     */
    protected abstract void defineEnenmy();

    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Este método se encarga de actualizar el comportamiento del enemigo en cada
     * frame.
     * 
     * @param dt El tiempo delta para la actualización.
     */
    public abstract void update(float dt);

    /**
     * Método que simula el impacto de un golpe en la cabeza del enemigo.
     * Las subclases deben proporcionar su propia implementación.
     */
    public abstract void hitOnHead();

    /**
     * Invierte la dirección de la velocidad del enemigo en el eje X y/o Y.
     * 
     * @param x Si es verdadero, invierte la dirección en el eje X.
     * @param y Si es verdadero, invierte la dirección en el eje Y.
     */
    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

}
