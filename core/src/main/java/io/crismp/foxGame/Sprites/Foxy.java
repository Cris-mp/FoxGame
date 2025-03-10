package io.crismp.foxGame.sprites;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
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
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.AssetsManagerAudio;
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.enemies.Enemy;
import io.crismp.foxGame.sprites.tileObjects.Pinchos;
import io.crismp.foxGame.tools.GamePreferences;

/**
 * Clase que representa al personaje principal del juego, Foxy.
 * Maneja el movimiento, las animaciones, las interacciones físicas y el estado
 * del personaje.
 */
public class Foxy extends Sprite {

	/**
	 * Enum que representa los posibles estados de Foxy.
	 */
	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, CLIMBING, CLIMB, HURT, DEAD
	}

	private static final float HURT_DURATION = 1f;

	protected PlayScreen screen;
	public State currenState;
	public State previoState;
	private float stateTimer;
	private boolean runRight;

	private Animation<TextureRegion> foxRun;
	private Animation<TextureRegion> foxSt;
	private Animation<TextureRegion> foxClimbing;
	private Animation<TextureRegion> foxHurt;

	TextureRegion foxJump;
	TextureRegion foxFall;
	TextureRegion foxDead;
	TextureRegion foxClimb;

	public World world;
	public Body body;
	public TiledMap map;
	public float velX, velY, speed;
	public int jumpCounter;
	public boolean onLadder;
	public boolean headInLadder;

	private boolean foxyIsHurt;
	private float hurtTimer;
	private boolean foxyIsDead;
	private boolean insideSecretRoom;
	public int life;
	public ArrayList<Enemy> enemiesInContact;
	public ArrayList<Pinchos> pinchosInContact;

	public Texture img;
	public boolean endGame;
	public boolean onRamp;

	// Getters y setters para los estados y variables
	public void setOnRamp(boolean onRamp) {
		this.onRamp = onRamp;
	}

	public Boolean getOnLadder() {
		return onLadder;
	}

	public void setOnLadder(Boolean inLadder) {
		onLadder = inLadder;
	}

	public void setHeadInLadder(Boolean inLadder) {
		headInLadder = inLadder;
	}

	public boolean isInsideSecretRoom() {
		return insideSecretRoom;
	}

	public void setInsideSecretRoom(boolean inside) {
		insideSecretRoom = inside;
	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public boolean isDead() {
		return foxyIsDead;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	/**
	 * Constructor de Foxy, inicializa el personaje con valores predeterminados y
	 * carga las animaciones.
	 * 
	 * @param screen La pantalla donde se encuentra Foxy.
	 */
	public Foxy(PlayScreen screen) {
		super(AssetsManager.getTexture("player/zorrito.png"), 16, 16);
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
		this.endGame = false;
		enemiesInContact = new ArrayList<>();
		pinchosInContact = new ArrayList<>();
		headInLadder = false;
		onRamp = false;
		insideSecretRoom = false;

		// Inicializar animaciones
		currenState = State.STANDING;
		previoState = State.STANDING;
		stateTimer = 0;
		runRight = true;

		Array<TextureRegion> frames = new Array<TextureRegion>();
		// RUN (animación de correr)
		for (int i = 1; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 2, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxRun = new Animation<>(0.1f, frames);
		frames.clear();

		// CLIMB (animación de escalar)
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 4, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxClimbing = new Animation<>(0.1f, frames);
		foxClimb = frames.get(0);
		frames.clear();

		// STAND (animación de estar parado)
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 0, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxSt = new Animation<>(0.1f, frames);
		frames.clear();

		// HURT (animación de herido)
		for (int i = 0; i < 2; i++) {
			frames.add(new TextureRegion(getTexture(), i * 16 * 2, 16 * 8, getTexture().getWidth() / 6,
					getTexture().getHeight() / 12));
		}
		foxHurt = new Animation<>(0.1f, frames);
		frames.clear();

		// Texturas no animadas
		foxDead = new TextureRegion(getTexture(), 0, 16 * 14, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);
		foxJump = new TextureRegion(getTexture(), 0, 16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);
		foxFall = new TextureRegion(getTexture(), 16 * 2, 16 * 10, getTexture().getWidth() / 6,
				getTexture().getHeight() / 12);

		defineFoxy();
		setBounds(0, 0, 16 / (FoxGame.PPM / 2), 16 / (FoxGame.PPM / 2));
	}

	/**
	 * Actualiza el estado y posición de Foxy en cada frame.
	 * 
	 * @param dt El tiempo transcurrido entre frames.
	 */

	public void update(float dt) {
		// Actualiza la posición en pantalla de Foxy
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 6);
		setRegion(getFrame(dt));

		// Lógica para manejar el estado de herido
		if (foxyIsHurt) {
			hurtTimer += dt; // Aumenta el tiempo en estado HURT
			if (hurtTimer >= HURT_DURATION) {
				foxyIsHurt = false; // Después de 1 segundos, vuelve a la normalidad
				if ((!enemiesInContact.isEmpty() || !pinchosInContact.isEmpty())) {
					hit(); // Si hay enemigos o pinchos en contacto, recibe daño
				}
			}
		}
	}

	/**
	 * Obtiene el frame de la animación correspondiente al estado actual.
	 * 
	 * @param delta El tiempo transcurrido en cada frame.
	 * @return El frame de la animación.
	 */
	public TextureRegion getFrame(float delta) {
		currenState = getState();
		TextureRegion region;
		// Determina qué animación mostrar según el estado actual
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
				region = (TextureRegion) foxClimbing.getKeyFrame(stateTimer, true);
				break;
			case CLIMB:
				region = foxClimb;
				break;
			case STANDING:
			default:
				region = (TextureRegion) foxSt.getKeyFrame(stateTimer, true);
				break;
		}

		// Voltea la imagen dependiendo de la dirección
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

	/**
	 * Determina el estado actual de Foxy basado en las condiciones físicas y el
	 * comportamiento.
	 * 
	 * @return El estado actual de Foxy.
	 */
	public State getState() {
		if (foxyIsDead) {
			return State.DEAD;
		}
		if (foxyIsHurt) {
			return State.HURT;
		}
		if (body.getLinearVelocity().y > 0 && !onLadder && !onRamp) {
			return State.JUMPING;
		}
		if (body.getLinearVelocity().y < 0 && !onLadder && !onRamp) {
			return State.FALLING;
		}
		if (body.getLinearVelocity().x != 0 && !onLadder) {
			return State.RUNNING;
		}
		if (body.getLinearVelocity().y != 0 && onLadder) {
			return State.CLIMBING;
		}
		if (body.getLinearVelocity().y == 0 && headInLadder) {
			return State.CLIMB;
		}
		return State.STANDING;
	}

	/**
	 * Maneja la colisión del personaje con enemigos o pinchos, causando daño a
	 * Foxy.
	 */
	public void hit() {
		if (!foxyIsHurt && !foxyIsDead) {
			foxyIsHurt = true;
			hurtTimer = 0;
			life--;
			screen.game.playSound(AssetsManagerAudio.getSound("audio/sounds/player/hit.ogg"));
			if (GamePreferences.isVibrationEnabled() && !foxyIsDead) {
				Gdx.input.vibrate(1000);
			}
			screen.restLife(life);
			if (life <= 0) {
				foxyIsDead = true;
				stateTimer = 0; // Para el tiempo de espera de "game over"
				Filter filter = new Filter();
				filter.maskBits = FoxGame.NOTHING_BIT;
				for (Fixture fix : body.getFixtureList())
					fix.setFilterData(filter);
				body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
			}
		}
	}

	/**
	 * Define el cuerpo físico de Foxy dentro del mundo del juego.
	 */
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
				FoxGame.WALL_BIT | FoxGame.OBSTACLE_BIT |
				FoxGame.ENEMY_BIT | FoxGame.SPIKES_BIT |
				FoxGame.LADDER_BIT | FoxGame.ITEM_BIT |
				FoxGame.ENEMY_HEAD_BIT | FoxGame.END_GAME_BIT |
				FoxGame.CARTEL_BIT | FoxGame.RAMP_BIT;
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0, 8 / FoxGame.PPM));

		// Cabeza de Foxy
		fdef.filter.categoryBits = FoxGame.FOX_HEAD_BIT;
		fdef.filter.maskBits = FoxGame.ENEMY_BIT | FoxGame.SPIKES_BIT |
				FoxGame.ITEM_BIT | FoxGame.OBSTACLE_BIT |
				FoxGame.FLOOR_BIT | FoxGame.LADDER_BIT | FoxGame.WALL_BIT |
				FoxGame.GROUND_BIT | FoxGame.SECRET_DOOR_BIT;
		Fixture headFixture = body.createFixture(fdef);
		headFixture.setUserData(this);
	}

	/**
	 * Activa o desactiva la visibilidad de la habitación secreta y sus elementos.
	 * 
	 * @param show Si es verdadero, muestra la habitación secreta, de lo contrario,
	 *             la oculta.
	 */
	public void toggleSecretRoom(boolean show) {
		MapLayer decoracionLayer = map.getLayers().get("SecretDecos");
		MapLayer decoracionLayer2 = map.getLayers().get("SecretDecos2");
		MapLayer habitacionLayer = map.getLayers().get("SalaSecreta");
		if (show) {
			decoracionLayer.setVisible(true);
			decoracionLayer2.setVisible(true);
			habitacionLayer.setVisible(true);
			// Activar los enemigos en la zona secreta
		} else {
			decoracionLayer.setVisible(false);
			decoracionLayer2.setVisible(false);
			habitacionLayer.setVisible(false);
		}
	}
}
