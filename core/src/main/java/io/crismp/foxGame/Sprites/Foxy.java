package io.crismp.foxGame.Sprites;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Screens.PlayScreen;
import io.crismp.foxGame.Sprites.enemies.Enemy;
import io.crismp.foxGame.Sprites.tileObjects.Pinchos;




public class Foxy extends Sprite {
	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, CLIMBING, HURT, DEAD
	}

	private static final float HURT_DURATION = 2f;

	protected PlayScreen screen;
	public State currenState;
	public State previoState;
	private float stateTimer;
	private boolean runRight;

	private Animation<TextureRegion> foxRun;
	private Animation<TextureRegion> foxSt;
	private Animation<TextureRegion> foxClimb;
	private Animation<TextureRegion> foxHurt;

	TextureRegion foxJump;
	TextureRegion foxFall;
	TextureRegion foxDead;

	public World world;
	public Body body;
	public TiledMap map;
	public float velX, velY, speed;
	public int jumpCounter;
	public boolean onLadder;
	private boolean foxyIsHurt;
	private float hurtTimer;
	private boolean foxyIsDead;
	public int life;
	public ArrayList<Enemy> enemiesInContact;
	public ArrayList<Pinchos> pinchosInContact;

	public Texture img;

	public boolean endGame;

	public Boolean getOnLadder() {
		return onLadder;
	}

	public void setOnLadder(Boolean inLadder) {
		onLadder = inLadder;
	}

	public Foxy(PlayScreen screen) {
		super(new Texture("player/zorritoExtendido.png"), 16, 16);
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.velX = 0;
		this.velY = 0;
		this.speed = 1.2f;
		this.jumpCounter = 0;
		this.life = 6;
		this.onLadder = false;
		this.foxyIsHurt = false;
		this.endGame=false;
		enemiesInContact = new ArrayList<>();
		pinchosInContact = new ArrayList<>();


		// animaciones
		currenState = State.STANDING;
		previoState = State.STANDING;
		stateTimer = 0;
		runRight = true;

		// RUN
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for (int i = 1; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 2, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxRun = new Animation<>(0.1f, frames);
		frames.clear();

		// CLIMB
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 4, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxClimb = new Animation<>(0.1f, frames);
		frames.clear();

		// STAND
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 0, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxSt = new Animation<>(0.1f, frames);
		frames.clear();

		// HURT
		for (int i = 0; i < 2; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 8, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxHurt = new Animation<>(0.1f, frames);
		frames.clear();

		foxDead = new TextureRegion(getTexture(), 0, 16 * 14, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);
		foxJump = new TextureRegion(getTexture(), 0, 16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);
		foxFall = new TextureRegion(getTexture(), 16 * 2, 16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);
		defineFoxy();
		setBounds(0, 0, 16 / (FoxGame.PPM / 2), 16 / (FoxGame.PPM / 2));

	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public void update(float dt) {
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 6);
		setRegion(getFrame(dt));

		if (foxyIsHurt) {
			hurtTimer += dt; // Aumenta el tiempo en estado HURT
			if (hurtTimer >= HURT_DURATION) {
				foxyIsHurt = false; // Después de 1 segundos, vuelve a la normalidad
				if ((!enemiesInContact.isEmpty()||!pinchosInContact.isEmpty())) {
					hit();
				}
			}
		}
	}

	public TextureRegion getFrame(float delta) {
		currenState = getState();
		TextureRegion region;
		switch (currenState) {
			case JUMPING:
				region = foxJump;
				break;
			case FALLING:
				region = foxFall;
				break;
			case DEAD:
				region = foxDead;
				break;
			case HURT:
				region = (TextureRegion) foxHurt.getKeyFrame(stateTimer, true);
				break;
			case RUNNING:
				region = (TextureRegion) foxRun.getKeyFrame(stateTimer, true);
				break;
			case CLIMBING:
				region = (TextureRegion) foxClimb.getKeyFrame(stateTimer, true);
				break;
			case STANDING:
			default:
				region = (TextureRegion) foxSt.getKeyFrame(stateTimer, true);
				break;
		}

		if ((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX()) {
			region.flip(true, false);
			runRight = false;
		} else if ((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX()) {
			region.flip(true, false);
			runRight = true;
		}

		stateTimer = currenState == previoState ? stateTimer + delta : 0;
		previoState = currenState;
		return region;

	}

	public State getState() {
		if (foxyIsDead) {
			return State.DEAD;
		}
		if (foxyIsHurt) {
			return State.HURT;
		}
		if (body.getLinearVelocity().y > 0 && !onLadder) {
			return State.JUMPING;
		}
		if (body.getLinearVelocity().y < 0 && !onLadder) {
			return State.FALLING;
		}
		if (body.getLinearVelocity().x != 0 && !onLadder) {
			return State.RUNNING;
		}
		if (body.getLinearVelocity().y > 0 && onLadder) {
			return State.CLIMBING;
		}
		return State.STANDING;
	}

	public boolean isDead() {
		return foxyIsDead;
	}
	public float getStateTimer(){
		return stateTimer;
	}

	public void hit() {
		if (!foxyIsHurt) {
			foxyIsHurt = true;
			hurtTimer = 0;
			life--;
            if(screen.getGame().isVibrationOn){
                Gdx.input.vibrate(1000);
            }
			screen.restLife(life);
			if (life <= 0) {
				foxyIsDead = true;
				Filter filter = new Filter();
				filter.maskBits = FoxGame.NOTHING_BIT;
				for (Fixture fix : body.getFixtureList())
					fix.setFilterData(filter);
				body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
			}
		}
	}

	public void defineFoxy() {
		BodyDef bdef = new BodyDef();
		Rectangle rectangle = ((RectangleMapObject) map.getLayers().get("player").getObjects().get(0)).getRectangle();
		bdef.position.set((rectangle.getX()) / FoxGame.PPM, (rectangle.getY()) / FoxGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5 / FoxGame.PPM);
		fdef.filter.categoryBits = FoxGame.FOX_BIT;
		fdef.filter.maskBits = FoxGame.GROUND_BIT | FoxGame.FLOOR_BIT |
							   FoxGame.WALL_BIT | FoxGame.OBSTACLE_BIT|
							   FoxGame.ENEMY_BIT | FoxGame.PINCHOS_BIT |
							   FoxGame.LADDER_BIT | FoxGame.ITEM_BIT|
							   FoxGame.ENEMY_HEAD_BIT|FoxGame.END_GAME_BIT;
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0, 8 / FoxGame.PPM));
		body.createFixture(fdef).setUserData(this);
	}

}
