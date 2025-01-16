package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import io.crismp.foxGame.FoxGame;

public class Foxy extends Sprite {
	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, CLIMBING
	}

	public State currenState;
	public State previoState;
	private float stateTimer;
	private boolean runRight;

	private Animation<TextureRegion> foxRun;
	private Animation<TextureRegion> foxSt;
	private Animation<TextureRegion> foxClimb;

	TextureRegion foxJump;
	TextureRegion foxFall;

	public World world;
	public Body body;
	public TiledMap map;
	public float velX, velY, speed;
	public int jumpCounter;
	public Boolean onLadder;

	public Texture img;

	public Boolean getOnLadder() {
		return onLadder;
	}

	public void setOnLadder(Boolean inLadder) {
		this.onLadder = inLadder;
	}

	public Foxy(World world, TiledMap map) {
		super(new Texture("player/zorrito.png"), 16, 16);
		this.world = world;
		this.map = map;
		this.velX = 0;
		this.velY = 0;
		this.speed = 2f;
		this.jumpCounter = 0;
		this.onLadder=false;

		// animaciones
		currenState = State.STANDING;
		previoState = State.STANDING;
		stateTimer = 0;
		runRight = true;

		// RUN
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for (int i = 1; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 2, getTexture().getWidth() / 6,
					getTexture().getHeight() / 6));
		}
		foxRun = new Animation<>(0.1f, frames);
		frames.clear();

		// CLIMB
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 2 * 3, getTexture().getWidth() / 6,
					getTexture().getHeight() / 6));
		}
		foxClimb = new Animation<>(0.1f, frames);
		frames.clear();

		// STAND
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 0, getTexture().getWidth() / 6,
					getTexture().getHeight() / 6));
		}
		foxSt = new Animation<>(0.1f, frames);
		frames.clear();

		foxJump = new TextureRegion(getTexture(), 0 ,16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 6);
		foxFall = new TextureRegion(getTexture(), 16*2, 16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 6);
		defineFoxy();
		setBounds(0, 0, 16 / (FoxGame.PPM / 2), 16 / (FoxGame.PPM / 2));

	}

	public void update(float dt) {
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 6);// NOTE: no entiendo
																									// porque tine que
																									// ser un 4
		setRegion(getFrame(dt));
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
		if (body.getLinearVelocity().y > 0) {
			return State.JUMPING;
		}
		if (body.getLinearVelocity().y < 0) {
			return State.FALLING;
		}
		if (body.getLinearVelocity().x != 0) {
			return State.RUNNING;
		}
		// if (body.getLinearVelocity().y > 0) {
		// return State.CLIMBING;
		// }
		return State.STANDING;
	}

	public void defineFoxy() {
		BodyDef bdef = new BodyDef();

		for (MapObject object : map.getLayers().get("player").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			// Posicionamos el cuerpo
			bdef.position.set((rectangle.getX()) / FoxGame.PPM, (rectangle.getY()) / FoxGame.PPM);
		}

		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5 / FoxGame.PPM);
		fdef.filter.categoryBits=FoxGame.FOX_BIT;
		fdef.filter.maskBits = FoxGame.DEFAULT_BIT|FoxGame.ZARZAS_BIT|FoxGame.LADDER_BIT;

		fdef.shape = shape;
		body.createFixture(fdef);
	


		//NOTA: sensor en la cabeza de foxy, no rompe ladrillos pero creo que sera util para dubir a las plataformas
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/FoxGame.PPM,14/FoxGame.PPM), new Vector2(2/FoxGame.PPM,14/FoxGame.PPM));
		fdef.shape = head;
		fdef.isSensor=true;
		body.createFixture(fdef).setUserData("head");

		

	}

}
