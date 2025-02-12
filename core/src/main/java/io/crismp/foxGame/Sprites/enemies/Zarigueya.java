package io.crismp.foxGame.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.AssetsManagerAudio;
import io.crismp.foxGame.screens.PlayScreen;

public class Zarigueya extends Enemy {
    public enum State {
        WALK, DEAD
    }

    public State currenState;
    private float stateTimer;
    private boolean runRight;

    private Animation<TextureRegion> zarWalk;
    private Animation<TextureRegion> zarDead;
    private Texture walk;
    private Texture dead;

    private boolean setToDestroy;
    private boolean destroyed;

    public Zarigueya(PlayScreen screen, Rectangle rect) {
        super(screen, rect);

        // --walk----
        walk = AssetsManager.getTexture("enemies/opossum.png");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(walk, i * 36, 0, walk.getWidth() / 6, walk.getHeight()));
        }
        zarWalk = new Animation<>(0.1f, frames);
        frames.clear();

        // --dead----
        dead = AssetsManager.getTexture("enemies/enemy-deadth.png");
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(dead, i * 40, 0, dead.getWidth() / 6,
                    dead.getHeight()));
        }
        zarDead = new Animation<>(0.1f, frames);

        setToDestroy = false;
        destroyed = false;
        runRight = true;
        currenState = State.WALK;
        stateTimer = 0;
        setBounds(0, 0, 16 / (FoxGame.PPM / 2), 11 / (FoxGame.PPM / 2));
    }

    public void update(float dt) {
        stateTimer += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            stateTimer = 0;
        }
        setRegion(getFrame(dt));
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
    }

    @Override
    protected void defineEnenmy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((rect.getX()) / FoxGame.PPM, (rect.getY()) / FoxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / FoxGame.PPM, 4 / FoxGame.PPM);
        fdef.filter.categoryBits = FoxGame.ENEMY_BIT;
        fdef.filter.maskBits = FoxGame.GROUND_BIT | FoxGame.FLOOR_BIT |
                               FoxGame.WALL_BIT | FoxGame.OBSTACLE_BIT |
                               FoxGame.LADDER_BIT | FoxGame.FOX_BIT |
                               FoxGame.SPIKES_BIT;
        fdef.shape = shape;
        fdef.density = 200f; // 🔥 Aumentar densidad para evitar que Foxy lo mueva
        fdef.friction = 2f; // 🔥 Aumentar fricción para que no resbale
        body.createFixture(fdef).setUserData(this);

        // hacemos parte superior
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10, 8).scl(1 / FoxGame.PPM);
        vertice[1] = new Vector2(10, 8).scl(1 / FoxGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / FoxGame.PPM);
        vertice[3] = new Vector2(0, 3).scl(1 / FoxGame.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 1f;
        fdef.filter.categoryBits = FoxGame.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = FoxGame.FOX_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTimer < 0.5) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        screen.game.playSound(AssetsManagerAudio.getSound("audio/sounds/player/Impact.wav"));
    }

    public TextureRegion getFrame(float delta) {
        currenState = getState();
        TextureRegion region;
        switch (currenState) {
            case WALK:
                region = (TextureRegion) zarWalk.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
            default:
                region = (TextureRegion) zarDead.getKeyFrame(stateTimer, true);
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }

        return region;
    }

    public State getState() {
        if (setToDestroy) {
            return State.DEAD;
        }
        return State.WALK;
    }
}
