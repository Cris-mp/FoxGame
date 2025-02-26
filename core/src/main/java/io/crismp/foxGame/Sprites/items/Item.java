package io.crismp.foxGame.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.Foxy;

public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    public Rectangle rect;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Constructor de la clase Item.
     * Inicializa el ítem y lo coloca en la pantalla.
     * 
     * @param screen Pantalla actual del juego.
     * @param rect Rectángulo que define la posición y tamaño del ítem.
     */
    public Item(PlayScreen screen, Rectangle rect){
        this.screen = screen;
        this.world = screen.getWorld();
        this.rect=rect;
        this.active = true;
        toDestroy = false;
        destroyed = false;


        setPosition(rect.getX(), rect.getY());
        setBounds(getX(), getY(), 16 / FoxGame.PPM, 16 / FoxGame.PPM);
        defineItem();
    }

     /**
     * Método abstracto que debe ser implementado por las subclases 
     * para definir las propiedades físicas del ítem.
     */
    public abstract void defineItem();

    /**
     * Método abstracto que debe ser implementado por las subclases 
     * para definir la acción que realiza el ítem al ser utilizado.
     * 
     * @param foxy El personaje que usa el ítem.
     */
    public abstract void use(Foxy foxy);

    /**
     * Actualiza el estado del ítem, como su destrucción si es necesario.
     * 
     * @param dt El tiempo transcurrido desde la última actualización.
     */
    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

     /**
     * Dibuja el ítem en la pantalla.
     * 
     * @param batch El lote de dibujos donde se renderiza el ítem.
     */
    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

     /**
     * Marca el ítem para ser destruido.
     */
    public void destroy(){
        toDestroy = true;
    }

}
