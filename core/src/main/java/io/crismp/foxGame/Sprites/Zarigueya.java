package io.crismp.foxGame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 4);
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
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / FoxGame.PPM);
		fdef.filter.categoryBits=FoxGame.ENEMY_BIT;
		fdef.filter.maskBits = FoxGame.GROUND_BIT|FoxGame.ZARZAS_BIT|FoxGame.LADDER_BIT|FoxGame.OBJECT_BIT|FoxGame.FOX_BIT;

		fdef.shape = shape;
		body.createFixture(fdef);

		//TODO: intentanto definir la cabeza

		BodyDef bdefhead = new BodyDef();

		for (MapObject object : map.getLayers().get("zarigueya").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
			// Posicionamos el cuerpo
			bdefhead.position.set((rectangle.getX() / FoxGame.PPM)-0.1f, (rectangle.getY() / FoxGame.PPM)+0.1f);
		}

		bdefhead.type = BodyDef.BodyType.DynamicBody;
		head = world.createBody(bdefhead);
		head.setGravityScale(0);
		

		FixtureDef fdefhead = new FixtureDef();
		CircleShape shapehead = new CircleShape();
		shapehead.setRadius(4 / FoxGame.PPM);
		fdefhead.filter.categoryBits=FoxGame.ENEMY_BIT;
		fdefhead.filter.maskBits = FoxGame.GROUND_BIT|FoxGame.ZARZAS_BIT|FoxGame.LADDER_BIT|FoxGame.OBJECT_BIT|FoxGame.FOX_BIT;

		fdefhead.shape = shapehead;
		head.createFixture(fdefhead);

		head = world.createBody(bdefhead);
    }
    
}
