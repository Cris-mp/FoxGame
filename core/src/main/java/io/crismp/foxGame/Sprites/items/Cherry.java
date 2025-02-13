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

public class Cherry extends Item {

    private Animation<TextureRegion> cherryAnimation;

    private Texture cherryTexture;
    private float stateTime;
    private Fixture fixture;
    private Sound itemSound;
    private boolean collected;

    public Cherry(PlayScreen screen, Rectangle rect) {
        super(screen, rect);
        collected = false;
        cherryTexture = AssetsManager.getTexture("items/cherry.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(cherryTexture, i * 21, 0, cherryTexture.getWidth() / 5,
                    cherryTexture.getHeight()));
        }
        for (int i = 5 - 1; i >= 0; i--) {
            frames.add(new TextureRegion(cherryTexture, i * 21, 0, cherryTexture.getWidth() / 5,
                    cherryTexture.getHeight()));
        }
        cherryAnimation = new Animation<>(0.1f, frames);

        stateTime = 0;
        itemSound = AssetsManagerAudio.getSound("audio/sounds/item/coin.ogg");
    }

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
        fdef.filter.maskBits = FoxGame.FOX_BIT| FoxGame.FOX_HEAD_BIT;

        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setSensor(true);
    }

    @Override
    public void use(Foxy foxy) {
        if (!collected) { // Asegura que solo se recoja una vez
            collected = true;
            screen.addCherry();
            screen.game.playSound(itemSound);
            destroy();
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
        setRegion(cherryAnimation.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

}
