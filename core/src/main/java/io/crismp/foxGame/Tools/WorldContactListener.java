package io.crismp.foxGame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Sprites.enemies.Enemy;
import io.crismp.foxGame.Sprites.tileObjects.Escalera;
import io.crismp.foxGame.Sprites.tileObjects.InteractiveTiledObject;
import io.crismp.foxGame.Sprites.Foxy;

//Que ocurre cuando dos accesorios de Box2D chocan entre si
public class WorldContactListener implements ContactListener {
    Foxy player;

    // inicio de conexion o colision
    @Override
    public void beginContact(Contact contact) {
        // No sabemos quien es A y B en una colision
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case FoxGame.ENEMY_HEAD_BIT | FoxGame.FOX_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;
            case FoxGame.ENEMY_BIT | FoxGame.OBSTACLE_BIT:
            case FoxGame.ENEMY_BIT | FoxGame.WALL_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);

                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);

                break;
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
                Gdx.app.log("FOXY","DIED");
            default:
                break;
        }

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
                    Foxy.setOnLadder(true);// NOTE:lo he hecho statico pero no creo que este esto muy bien(si eso lo
                                           // reparo luego)
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

                    Foxy.setOnLadder(false);
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
