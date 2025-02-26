package io.crismp.foxGame.sprites.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.AssetsManagerAudio;
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.Foxy;

/**
 * Representa una gema que el personaje puede recoger en el juego.
 * La gema tiene una animación que se reproduce mientras está activa y puede ser recolectada.
 */
public class Gem extends Item {
    private Animation<TextureRegion> gemAnimation;
    private Texture gemTexture;
    private float stateTime;
    private Fixture fixture;
    private Sound itemSound;
    private boolean collected;
    private boolean inSecretRoom;

    public boolean isInSecretRoom() {
        return inSecretRoom;
    }

    public void setInSecretRoom(boolean inSecretRoom) {
        this.inSecretRoom = inSecretRoom;
    }

    /**
     * Constructor de la gema. Inicializa la animación, el sonido y el estado de la
     * gema.
     * 
     * @param screen Pantalla actual del juego.
     * @param rect   Rectángulo que define la posición y tamaño de la gema.
     */
    public Gem(PlayScreen screen, Rectangle rect) {
        super(screen, rect);
        collected = false;
        inSecretRoom = false;
        gemTexture = AssetsManager.getTexture("items/gem.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(gemTexture, i * 15, 0, gemTexture.getWidth() / 5,
                    gemTexture.getHeight()));
        }
        gemAnimation = new Animation<>(0.1f, frames);

        stateTime = 0;
        itemSound = AssetsManagerAudio.getSound("audio/sounds/item/gem.ogg");

    }

    /**
     * Define el comportamiento físico y de colisiones de la gema en el mundo.
     */
    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();

        bdef.position.set((rect.getX()) / FoxGame.PPM, (rect.getY()) / FoxGame.PPM);

        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / FoxGame.PPM, 5 / FoxGame.PPM);
        fdef.filter.categoryBits = FoxGame.ITEM_BIT;
        fdef.filter.maskBits = FoxGame.FOX_BIT | FoxGame.FOX_HEAD_BIT;

        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setSensor(true);
    }

    /**
     * Realiza la acción de recoger la gema y destruirla.
     *
     * @param foxy El personaje que interactúa con la gema
     */
    @Override
    public void use(Foxy foxy) {
        if (!collected) { // Asegura que solo se recoja una vez
            collected = true;
            screen.addGem();
            screen.game.playSound(itemSound);
            destroy();
        }
    }

    /**
     * Actualiza el estado de la gema, incluyendo la animación y la posición.
     *
     * @param dt El tiempo transcurrido desde la última actualización
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
        setRegion(gemAnimation.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
}
