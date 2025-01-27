package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Sprites.Foxy.State;
import io.crismp.foxGame.screens.PlayScreen;

public class Zarigueya extends Enemy {

    public State StateTime;
	private float stateTimer;
	private Animation<TextureRegion> zarWalk;
    private Texture zar;

	public Body head;



    public Zarigueya(PlayScreen screen,float y, float x) {
        super(screen,x,y);
        zar=new Texture("enemies/opossum.png");


        Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 6; i++) {
			frames.add(new TextureRegion(zar, i * 36, 0 , zar.getWidth()/6, zar.getHeight()));
		}
		zarWalk = new Animation<>(0.1f, frames);
		stateTimer=0;
        setBounds(0, 0, 16 / (FoxGame.PPM / 2), 12 / (FoxGame.PPM / 2));


    }
    public void update(float dt){
        stateTimer+=dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
		setRegion(zarWalk.getKeyFrame(stateTimer,true));
    }

    @Override
    protected void defineEnenmy() {
        BodyDef bdef = new BodyDef();

		for (MapObject object : map.getLayers().get("zarigueya").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			// Posicionamos el cuerpo
			bdef.position.set((rectangle.getX()) / FoxGame.PPM, (rectangle.getY()) / FoxGame.PPM);
		}

		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
        shape.setAsBox( 12/FoxGame.PPM, 6/FoxGame.PPM);
		fdef.filter.categoryBits=FoxGame.ENEMY_BIT;
		fdef.filter.maskBits = FoxGame.GROUND_BIT|FoxGame.ZARZAS_BIT|FoxGame.LADDER_BIT|FoxGame.OBJECT_BIT|FoxGame.FOX_BIT;

		fdef.shape = shape;
		body.createFixture(fdef);

       //hacemos parte superior
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10, 7).scl(1 / FoxGame.PPM);
        vertice[1] = new Vector2(10, 7).scl(1 / FoxGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / FoxGame.PPM);
        vertice[3] = new Vector2(0, 3).scl(1 / FoxGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1f;
        fdef.filter.categoryBits = FoxGame.ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);



    }

}
