package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;

public class Zarigueya extends Enemy {
    public enum State {
        WALK, DEAD
    }

    public State currenState;
    public State previoState;
    private float stateTimer;
    private boolean runRight;

    // public State StateTime;
    private Animation<TextureRegion> zarWalk;
    private Animation<TextureRegion> dead;
    private Texture zar;

    private boolean setToDestroy;
    private boolean destroyed;
   

    public Zarigueya(PlayScreen screen, Rectangle rect) {
        super(screen,rect);
       
        zar = new Texture("enemies/opossum.png");

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(zar, i * 36, 0, zar.getWidth() / 6, zar.getHeight()));
        }
        zarWalk = new Animation<>(0.1f, frames);
        stateTimer = 0;
        setBounds(0, 0, 16 / (FoxGame.PPM / 2), 12 / (FoxGame.PPM / 2));
        setToDestroy = false;
        destroyed = false;
        // dead
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(new Texture("enemies/enemy-deadth.png"), i * 40, 0, zar.getWidth() / 6,
                    zar.getHeight()));
        }
        dead = new Animation<>(0.4f, frames);

        // animaciones
        currenState = State.WALK;
        previoState = State.WALK;
        runRight = true;

    }

    public void update(float dt) {
        // TODO: problema al hacer la animacion de muerte
        stateTimer += dt;
        setRegion(getFrame(dt));

        if (setToDestroy && !destroyed) {
                world.destroyBody(body);
                destroyed = true;
                stateTimer = 0; 
        } else if (!destroyed) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
        }
    }

    @Override
    protected void defineEnenmy() {
        BodyDef bdef = new BodyDef();

        
            bdef.position.set((rect.getX()) / FoxGame.PPM, (rect.getY()) / FoxGame.PPM);
        

        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / FoxGame.PPM, 5 / FoxGame.PPM);
        fdef.filter.categoryBits = FoxGame.ENEMY_BIT;
        fdef.filter.maskBits = FoxGame.GROUND_BIT | FoxGame.FLOOR_BIT | FoxGame.WALL_BIT | FoxGame.OBSTACLE_BIT
                | FoxGame.LADDER_BIT | FoxGame.FOX_BIT|FoxGame.ZARZAS_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        // hacemos parte superior
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10, 6).scl(1 / FoxGame.PPM);
        vertice[1] = new Vector2(10, 6).scl(1 / FoxGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / FoxGame.PPM);
        vertice[3] = new Vector2(0, 3).scl(1 / FoxGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = FoxGame.ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTimer < 1) {
            super.draw(batch);
        }

    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
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
                region = (TextureRegion) dead.getKeyFrame(10f, false);
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }
        previoState = currenState;
        return region;
    }

    public State getState() {
        if (body.getLinearVelocity().x != 0 && !setToDestroy) {
            return State.WALK;
        } else if (setToDestroy) {
            return State.DEAD;
        } else {
            return State.WALK;
        }
    }

}
