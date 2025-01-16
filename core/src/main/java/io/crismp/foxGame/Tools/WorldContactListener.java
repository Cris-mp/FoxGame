package io.crismp.foxGame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.crismp.foxGame.Sprites.Escalera;
import io.crismp.foxGame.Sprites.Foxy;
import io.crismp.foxGame.Sprites.InteractiveTiledObject;

//Que ocurre cuando dos accesorios de Box2D chocan entre si
public class WorldContactListener implements ContactListener {
    Foxy player;

    // inicio de conexion o colision
    @Override
    public void beginContact(Contact contact) {
        // No sabemos quien es A y B en una colision
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // si colisiona con la cabeza
        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            // vemos cual es el elemento cabeza
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixB ? fixA : fixB;

            // vemos con que objeto estamos colisionando --> si no es nulo y se le puede
            // asignar la clase InteractiveTiledobject
            if (object.getUserData() != null
                    && InteractiveTiledObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTiledObject) object.getUserData()).onHeadHit();
                if (((InteractiveTiledObject) object.getUserData()).getClass().equals(Escalera.class)) {
                    player = (Foxy) head.getUserData();
                    player.setOnLadder(true);
                    Gdx.app.log("true", "Escalera");
                }
            }
        }
    }

    // desconexion
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // si colisiona con la cabeza
        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            // vemos cual es el elemento cabeza
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixB ? fixA : fixB;

            // vemos con que objeto estamos colisionando --> si no es nulo y se le puede
            // asignar la clase InteractiveTiledobject
            if (object.getUserData() != null
                    && InteractiveTiledObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTiledObject) object.getUserData()).onHeadHit();
                if (((InteractiveTiledObject) object.getUserData()).getClass().equals(Escalera.class)) {
                    player = (Foxy) head.getUserData();
                    player.setOnLadder(false);
                    Gdx.app.log("false", "Escalera");
                }
            }
        }
    }

    // Una vez ha chocado puede cambiar las caracteristicas
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    // resultado de la colision
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
