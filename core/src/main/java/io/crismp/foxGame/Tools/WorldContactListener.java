package io.crismp.foxGame.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.Sprites.enemies.Enemy;
import io.crismp.foxGame.Sprites.items.Item;
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
                System.out.println("A" + fixA.getFilterData().categoryBits);
                System.out.println("B" + fixB.getFilterData().categoryBits);
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
            case FoxGame.FOX_BIT | FoxGame.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Foxy) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Foxy) fixA.getUserData());
                break;
            case FoxGame.FOX_BIT | FoxGame.PINCHOS_BIT:
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
                System.out.println("A" + fixA.getFilterData().categoryBits);
                System.out.println("B" + fixB.getFilterData().categoryBits);
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                    ((Foxy) fixA.getUserData()).hit();
                else {
                    ((Foxy) fixB.getUserData()).hit();
                }
                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else if(fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.LADDER_BIT ||
                        fixB.getFilterData().categoryBits == FoxGame.LADDER_BIT) {
                    if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                        ((Foxy) fixA.getUserData()).setOnLadder(true);
                    else
                        ((Foxy) fixB.getUserData()).setOnLadder(true);
                    break;
                }
            default:
                break;
        }

    }

    // desconexion
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                    ((Foxy) fixA.getUserData()).setOnLadder(false);
                else
                    ((Foxy) fixB.getUserData()).setOnLadder(false);
                break;
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
