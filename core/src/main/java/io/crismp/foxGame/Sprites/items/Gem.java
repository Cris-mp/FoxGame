package io.crismp.foxGame.sprites.items;

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
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.Foxy;

public class Gem extends Item{
    private Animation<TextureRegion> gemAnimation;
    private Texture gemTexture;
    private float stateTime;
    private Fixture fixture;

    public Gem(PlayScreen screen, Rectangle rect) {
        super(screen, rect);

        gemTexture = AssetsManager.getTexture("items/gem.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(gemTexture, i * 15, 0, gemTexture.getWidth() / 5,
                    gemTexture.getHeight()));
        }
        gemAnimation = new Animation<>(0.1f, frames);

        stateTime = 0;

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
        fdef.filter.maskBits = FoxGame.FOX_BIT;

        fdef.shape = shape;
        fixture=body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setSensor(true);
    }

    @Override
    public void use(Foxy foxy) {
        screen.addGem();
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
        setRegion(gemAnimation.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }
}
