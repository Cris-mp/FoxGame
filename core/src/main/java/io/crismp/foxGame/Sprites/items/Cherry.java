package io.crismp.foxGame.Sprites.items;

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
import io.crismp.foxGame.Screens.PlayScreen;
import io.crismp.foxGame.Sprites.Foxy;
import io.crismp.foxGame.Tools.AssetsManager;

public class Cherry extends Item {

    private Animation<TextureRegion> cherryAnimation;
    private Texture cherryTexture;
    private float stateTime;
    private Fixture fixture;

    public Cherry(PlayScreen screen, Rectangle rect) {
        super(screen, rect);

        cherryTexture = AssetsManager.getTexture("items/cherry.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(cherryTexture, i * 21, 0, cherryTexture.getWidth() / 5,
                    cherryTexture.getHeight()));
        }
        for (int i = 5-1; i >= 0; i--) {
            frames.add(new TextureRegion(cherryTexture, i * 21, 0, cherryTexture.getWidth() / 5,
                    cherryTexture.getHeight()));
        }
        cherryAnimation = new Animation<>(0.1f, frames);

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
        screen.addCherry();
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        stateTime += dt;
        setRegion(cherryAnimation.getKeyFrame(stateTime, true));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

}
