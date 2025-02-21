package io.crismp.foxGame.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.screens.PlayScreen;
import io.crismp.foxGame.sprites.Foxy;
import io.crismp.foxGame.sprites.enemies.Enemy;
import io.crismp.foxGame.sprites.items.Item;
import io.crismp.foxGame.sprites.tileObjects.Pinchos;

//Que ocurre cuando dos accesorios de Box2D chocan entre si
public class WorldContactListener implements ContactListener {
    PlayScreen screen;

    public WorldContactListener(PlayScreen screen) {
        this.screen = screen;
    }

    // inicio de conexion o colision
    @Override
    public void beginContact(Contact contact) {
        // No sabemos quien es A y B en una colision
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        System.out.println("Colisión detectada: " + fixA.getFilterData().categoryBits
                + " con "
                + fixB.getFilterData().categoryBits);
        switch (cDef) {
            case FoxGame.FOX_BIT | FoxGame.RAMP_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                    ((Foxy) fixA.getUserData()).setOnRamp(true);
                else
                    ((Foxy) fixB.getUserData()).setOnRamp(true);
            case FoxGame.FOX_BIT | FoxGame.GROUND_BIT:
            case FoxGame.FOX_BIT | FoxGame.OBSTACLE_BIT:
                screen.colision = true;
                break;
            case FoxGame.FOX_BIT | FoxGame.ENEMY_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;
            case FoxGame.ENEMY_BIT | FoxGame.OBSTACLE_BIT:
            case FoxGame.ENEMY_BIT | FoxGame.WALL_BIT:
            case FoxGame.ENEMY_BIT | FoxGame.SPIKES_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case FoxGame.FOX_HEAD_BIT | FoxGame.ITEM_BIT:
            case FoxGame.FOX_BIT | FoxGame.ITEM_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Foxy) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Foxy) fixA.getUserData());
                break;

            case FoxGame.FOX_BIT | FoxGame.SPIKES_BIT:
                screen.colision = true;
            case FoxGame.FOX_HEAD_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
                Foxy foxy = null;
                Enemy enemy = null;
                Pinchos pinchos = null;
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT
                        || fixA.getFilterData().categoryBits == FoxGame.FOX_HEAD_BIT) {
                    foxy = (Foxy) fixA.getUserData();
                    if (fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
                        enemy = (Enemy) fixB.getUserData();
                    } else {
                        pinchos = (Pinchos) fixB.getUserData();
                    }
                } else {
                    foxy = (Foxy) fixB.getUserData();
                    if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
                        enemy = (Enemy) fixA.getUserData();
                    } else {
                        pinchos = (Pinchos) fixA.getUserData();
                    }
                }
                if (enemy != null && !foxy.enemiesInContact.contains(enemy)) {
                    foxy.enemiesInContact.add(enemy); // Añadir enemigo a la lista de colisiones
                }
                if (pinchos != null && !foxy.pinchosInContact.contains(pinchos)) {
                    foxy.pinchosInContact.add(pinchos); // Añadir pinchos a la lista de colisiones
                }

                foxy.hit();

                if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else if (fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT)
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.LADDER_BIT ||
                        fixB.getFilterData().categoryBits == FoxGame.LADDER_BIT) {
                    if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                        ((Foxy) fixA.getUserData()).setOnLadder(true);
                    else
                        ((Foxy) fixB.getUserData()).setOnLadder(true);
                }
                break;
            case FoxGame.FOX_HEAD_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_HEAD_BIT)
                    ((Foxy) fixA.getUserData()).setHeadInLadder(true);
                else
                    ((Foxy) fixB.getUserData()).setHeadInLadder(true);

                break;

            case FoxGame.FOX_BIT | FoxGame.END_GAME_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.END_GAME_BIT ||
                        fixB.getFilterData().categoryBits == FoxGame.END_GAME_BIT) {
                    if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                        ((Foxy) fixA.getUserData()).setEndGame(true);
                    else
                        ((Foxy) fixB.getUserData()).setEndGame(true);
                }
                break;
            case FoxGame.FOX_BIT | FoxGame.CARTEL_BIT:

                int cartelID = 0;
                if (fixA.getFilterData().categoryBits == FoxGame.CARTEL_BIT) {
                    cartelID = (int) fixA.getUserData(); // Obtener ID desde Tiled
                } else {
                    cartelID = (int) fixB.getUserData();
                }
                System.out.println("Colisión con cartel " + cartelID);
                screen.hud.showCartel(cartelID); // Muestra la etiqueta correcta
                break;
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
            case FoxGame.FOX_HEAD_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_HEAD_BIT)
                    ((Foxy) fixA.getUserData()).setHeadInLadder(false);
                else
                    ((Foxy) fixB.getUserData()).setHeadInLadder(false);

                break;
            case FoxGame.FOX_BIT | FoxGame.RAMP_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT) {
                    ((Foxy) fixA.getUserData()).setOnRamp(false);
                    screen.colision = false;
                } else {
                    ((Foxy) fixB.getUserData()).setOnRamp(false);
                    screen.colision = false;
                }

                break;
            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT)
                    ((Foxy) fixA.getUserData()).setOnLadder(false);
                else
                    ((Foxy) fixB.getUserData()).setOnLadder(false);
                break;

            case FoxGame.FOX_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
            case FoxGame.FOX_HEAD_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_HEAD_BIT | FoxGame.ENEMY_BIT:
                Foxy foxy;
                Enemy enemy;
                Pinchos pinchos;
                if (fixA.getFilterData().categoryBits == FoxGame.FOX_BIT
                        || fixA.getFilterData().categoryBits == FoxGame.FOX_HEAD_BIT) {
                    foxy = (Foxy) fixA.getUserData();
                    if (fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
                        enemy = (Enemy) fixB.getUserData();
                        foxy.enemiesInContact.remove(enemy);
                    } else {
                        pinchos = (Pinchos) fixB.getUserData();
                        foxy.pinchosInContact.remove(pinchos);
                    }
                } else {
                    foxy = (Foxy) fixB.getUserData();
                    if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
                        enemy = (Enemy) fixA.getUserData();
                        foxy.enemiesInContact.remove(enemy);
                    } else {
                        pinchos = (Pinchos) fixA.getUserData();
                        foxy.pinchosInContact.remove(pinchos);
                    }
                }
                break;
            case FoxGame.FOX_BIT | FoxGame.CARTEL_BIT:
                int cartelID = 0;

                if (fixA.getFilterData().categoryBits == FoxGame.CARTEL_BIT) {
                    cartelID = (int) fixA.getUserData();
                } else {
                    cartelID = (int) fixB.getUserData();
                }

                screen.hud.hideCartel(cartelID); // Oculta la etiqueta correcta
                break;
            default:
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
