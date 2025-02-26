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

/**
 * Representa a una zarigueya enemiga en el juego.
 * La zarigueya tiene un estado de caminar o estar muerta, y se mueve dentro del
 * mundo en función de su estado. Puede ser destruida cuando se le golpea en la
 * cabeza y tiene animaciones para su caminar y su muerte.
 */
public class Zarigueya extends Enemy {
    /**
     * Enum que define los posibles estados de la zarigueya: caminando o muerta.
     */
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
    private boolean inSecretRoom;

    /**
     * Verifica si la zarigueya está en una sala secreta.
     *
     * @return True si la zarigueya está en una sala secreta, false en caso
     *         contrario.
     */
    public boolean isInSecretRoom() {
        return inSecretRoom;
    }

    /**
     * Establece si la zarigueya está en una sala secreta.
     *
     * @param inSecretRoom El estado que indica si la zarigueya está en una sala
     *                     secreta.
     */
    public void setInSecretRoom(boolean inSecretRoom) {
        this.inSecretRoom = inSecretRoom;
    }

    /**
     * Constructor de la zarigueya, que inicializa su animación, su estado y otras
     * propiedades.
     *
     * @param screen La pantalla donde se encuentra la zarigueya.
     * @param rect   El rectángulo que define la posición y tamaño de la zarigueya.
     */
    public Zarigueya(PlayScreen screen, Rectangle rect) {
        super(screen, rect);

        // Inicializa la animación de caminar
        Array<TextureRegion> frames = new Array<TextureRegion>();
        if (zarWalk == null) {
            walk = AssetsManager.getTexture("enemies/opossum.png");
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(walk, i * 36, 0, walk.getWidth() / 6, walk.getHeight()));
            }
            zarWalk = new Animation<>(0.1f, frames);
            frames.clear();
        }
        if (zarDead == null) {
            // Inicializa la animación de muerte
            dead = AssetsManager.getTexture("enemies/enemy-deadth.png");
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(dead, i * 40, 0, dead.getWidth() / 6,
                        dead.getHeight()));
            }
            zarDead = new Animation<>(0.1f, frames);
        }

        // Configuración inicial
        currenState = State.WALK;
        stateTimer = 0;
        setToDestroy = false;
        destroyed = false;
        runRight = true;
        inSecretRoom = false;

        // Establece los límites del sprite de la zarigueya
        setBounds(0, 0, 16 / (FoxGame.PPM / 2), 11 / (FoxGame.PPM / 2));
    }

    /**
     * Actualiza el estado de la zarigueya, incluyendo su animación y movimiento.
     *
     * @param dt El tiempo transcurrido desde la última actualización.
     */
    public void update(float dt) {
        stateTimer += dt;

        // Si la zarigueya está programada para ser destruida, destrúyela
        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            stateTimer = 0;
        }
        // Actualiza la animación de la zarigueya
        setRegion(getFrame(dt));
        body.setLinearVelocity(velocity);

        // Establece la posición del sprite basado en el cuerpo físico
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
    }

    /**
     * Define las propiedades físicas del enemigo, como su cuerpo y sus colisiones.
     */
    @Override
    protected void defineEnenmy() {
        // Configura el cuerpo físico de la zarigueya
        BodyDef bdef = new BodyDef();
        bdef.position.set((rect.getX()) / FoxGame.PPM, (rect.getY()) / FoxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // Define la forma de la zarigueya para la colisión
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / FoxGame.PPM, 4 / FoxGame.PPM);
        fdef.filter.categoryBits = FoxGame.ENEMY_BIT;
        fdef.filter.maskBits = FoxGame.GROUND_BIT | FoxGame.FLOOR_BIT |
                FoxGame.WALL_BIT | FoxGame.OBSTACLE_BIT |
                FoxGame.LADDER_BIT | FoxGame.FOX_BIT |
                FoxGame.SPIKES_BIT;
        fdef.shape = shape;
        fdef.density = 200f; // Aumentar densidad para evitar que Foxy lo mueva
        fdef.friction = 2f; // Aumentar fricción para que no resbale
        body.createFixture(fdef).setUserData(this);

        // Define la forma de la cabeza de la zarigueya para la colisión
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10, 8).scl(1 / FoxGame.PPM);
        vertice[1] = new Vector2(10, 8).scl(1 / FoxGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / FoxGame.PPM);
        vertice[3] = new Vector2(0, 3).scl(1 / FoxGame.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 1f; // Rebote cuando colisiona con el jugador
        fdef.filter.categoryBits = FoxGame.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = FoxGame.FOX_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Dibuja la zarigueya en la pantalla.
     * Si ha sido destruida, no se dibuja, excepto si el temporizador de estado es
     * menor a 0.5 segundos.
     *
     * @param batch El lote de dibujos donde se renderiza la zarigueya.
     */
    public void draw(Batch batch) {
        if (!destroyed || stateTimer < 0.5) {
            super.draw(batch);
        }
    }

    /**
     * Procesa el golpe en la cabeza de la zarigueya, provocando su destrucción.
     */
    @Override
    public void hitOnHead() {
        setToDestroy = true;
        screen.game.playSound(AssetsManagerAudio.getSound("audio/sounds/player/Impact.ogg"));
    }

    /**
     * Obtiene el cuadro de la animación que debe mostrarse, según el estado de la
     * zarigueya.
     *
     * @param delta El tiempo transcurrido desde la última actualización.
     * @return El cuadro de la animación correspondiente al estado actual.
     */
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

        // Cambia la dirección de la animación dependiendo de la dirección de movimiento
        if ((body.getLinearVelocity().x < 0 || !runRight) && region.isFlipX()) {
            region.flip(true, false);
            runRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runRight) && !region.isFlipX()) {
            region.flip(true, false);
            runRight = true;
        }

        return region;
    }

    /**
     * Obtiene el estado actual de la zarigueya (caminar o muerto).
     *
     * @return El estado actual de la zarigueya.
     */
    public State getState() {
        return setToDestroy ? State.DEAD : State.WALK;
    }

}
