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

/**
 * Clase que maneja la detección de colisiones en el mundo del juego.
 * Implementa la interfaz {@link ContactListener} para gestionar eventos de
 * colisión y separación entre objetos.
 */
public class WorldContactListener implements ContactListener {
    PlayScreen screen;

    /**
     * Constructor de WorldContactListener.
     *
     * @param screen Pantalla del juego donde ocurren las colisiones.
     */
    public WorldContactListener(PlayScreen screen) {
        this.screen = screen;
    }

    /**
     * Método llamado cuando dos objetos comienzan a colisionar.
     *
     * @param contact Información sobre la colisión.
     */
    @Override
    public void beginContact(Contact contact) {
        // No sabemos quien es A y B en una colision
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        // System.out.println("Colisión detectada: " + fixA.getFilterData().categoryBits
        // + " con "
        // + fixB.getFilterData().categoryBits);
        switch (collisionDef) {
            case FoxGame.FOX_HEAD_BIT | FoxGame.SECRET_DOOR_BIT:
                getFoxy(fixA, fixB).setInsideSecretRoom(true);
                break;

            case FoxGame.FOX_BIT | FoxGame.RAMP_BIT:
                getFoxy(fixA, fixB).setOnRamp(true);
                screen.colision = true;
                break;

            case FoxGame.FOX_BIT | FoxGame.GROUND_BIT:
            case FoxGame.FOX_BIT | FoxGame.OBSTACLE_BIT:
                screen.colision = true;
                getFoxy(fixA, fixB).setOnRamp(false);
                break;

            case FoxGame.FOX_BIT | FoxGame.ENEMY_HEAD_BIT:
                getEnemy(fixA, fixB).hitOnHead();
                break;

            case FoxGame.ENEMY_BIT | FoxGame.OBSTACLE_BIT:
            case FoxGame.ENEMY_BIT | FoxGame.WALL_BIT:
            case FoxGame.ENEMY_BIT | FoxGame.SPIKES_BIT:
                getEnemy(fixA, fixB).reverseVelocity(true, false);
                break;

            case FoxGame.FOX_HEAD_BIT | FoxGame.ITEM_BIT:
            case FoxGame.FOX_BIT | FoxGame.ITEM_BIT:
                getItem(fixA, fixB).use(getFoxy(fixA, fixB));
                break;

            case FoxGame.FOX_BIT | FoxGame.SPIKES_BIT:
                screen.colision = true;
            case FoxGame.FOX_HEAD_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
                handleDamageCollision(fixA, fixB);
                break;

            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                getFoxy(fixA, fixB).setOnLadder(true);
                break;

            case FoxGame.FOX_HEAD_BIT | FoxGame.LADDER_BIT:
                getFoxy(fixA, fixB).setHeadInLadder(true);
                break;

            case FoxGame.FOX_BIT | FoxGame.END_GAME_BIT:
                getFoxy(fixA, fixB).setEndGame(true);
                break;

            case FoxGame.FOX_BIT | FoxGame.CARTEL_BIT:
                screen.hud.showCartel(getCartelID(fixA, fixB));
                break;
            default:
                break;
        }
    }

    /**
     * Método llamado cuando dos objetos terminan su colisión.
     *
     * @param contact Información sobre la colisión terminada.
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int collisionDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collisionDef) {
            case FoxGame.FOX_HEAD_BIT | FoxGame.SECRET_DOOR_BIT:
                getFoxy(fixA, fixB).setInsideSecretRoom(false);
                break;

            case FoxGame.FOX_BIT | FoxGame.RAMP_BIT:
                getFoxy(fixA, fixB).setOnRamp(false);
                screen.colision = false;
                break;

            case FoxGame.FOX_BIT | FoxGame.LADDER_BIT:
                getFoxy(fixA, fixB).setOnLadder(false);
                break;
            case FoxGame.FOX_HEAD_BIT | FoxGame.LADDER_BIT:
                getFoxy(fixA, fixB).setHeadInLadder(false);
                break;

            case FoxGame.FOX_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_BIT | FoxGame.ENEMY_BIT:
            case FoxGame.FOX_HEAD_BIT | FoxGame.SPIKES_BIT:
            case FoxGame.FOX_HEAD_BIT | FoxGame.ENEMY_BIT:
                handleEndDamageCollision(fixA, fixB);
                break;

            case FoxGame.FOX_BIT | FoxGame.CARTEL_BIT:
                screen.hud.hideCartel(getCartelID(fixA, fixB));
                break;
            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    /**
     * Obtiene el objeto Foxy de la colisión.
     */
    private Foxy getFoxy(Fixture fixA, Fixture fixB) {
        return (Foxy) ((fixA.getFilterData().categoryBits == FoxGame.FOX_BIT
                || fixA.getFilterData().categoryBits == FoxGame.FOX_HEAD_BIT)
                        ? fixA.getUserData()
                        : fixB.getUserData());
    }

    /**
     * Obtiene el enemigo de la colisión.
     */
    private Enemy getEnemy(Fixture fixA, Fixture fixB) {
        return (Enemy) ((fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT
                || fixA.getFilterData().categoryBits == FoxGame.ENEMY_HEAD_BIT)
                        ? fixA.getUserData()
                        : fixB.getUserData());
    }

    /**
     * Obtiene el objeto de tipo Item de la colisión.
     */
    private Item getItem(Fixture fixA, Fixture fixB) {
        return (Item) ((fixA.getFilterData().categoryBits == FoxGame.ITEM_BIT) ? fixA.getUserData()
                : fixB.getUserData());
    }

    /**
     * Obtiene el ID del cartel en la colisión.
     */
    private int getCartelID(Fixture fixA, Fixture fixB) {
        return (int) ((fixA.getFilterData().categoryBits == FoxGame.CARTEL_BIT) ? fixA.getUserData()
                : fixB.getUserData());
    }

    /**
     * Maneja las colisiones que dañan al jugador, como enemigos y pinchos.
     */
    private void handleDamageCollision(Fixture fixA, Fixture fixB) {
        Foxy foxy = getFoxy(fixA, fixB);
        Enemy enemy = null;
        Pinchos pinchos = null;

        if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
            enemy = (Enemy) fixA.getUserData();
        } else if (fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
            enemy = (Enemy) fixB.getUserData();
        } else {
            pinchos = (Pinchos) ((fixA.getFilterData().categoryBits == FoxGame.SPIKES_BIT) ? fixA.getUserData()
                    : fixB.getUserData());
        }

        if (enemy != null && !foxy.enemiesInContact.contains(enemy)) {
            foxy.enemiesInContact.add(enemy);
        }
        if (pinchos != null && !foxy.pinchosInContact.contains(pinchos)) {
            foxy.pinchosInContact.add(pinchos);
        }

        foxy.hit();
        if (enemy != null)
            enemy.reverseVelocity(true, false);
    }

    /**
     * Maneja la finalización del contacto con objetos dañinos, eliminándolos de las
     * listas de colisión.
     */
    private void handleEndDamageCollision(Fixture fixA, Fixture fixB) {
        Foxy foxy = getFoxy(fixA, fixB);
        Enemy enemy = null;
        Pinchos pinchos = null;

        if (fixA.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
            enemy = (Enemy) fixA.getUserData();
        } else if (fixB.getFilterData().categoryBits == FoxGame.ENEMY_BIT) {
            enemy = (Enemy) fixB.getUserData();
        } else {
            pinchos = (Pinchos) ((fixA.getFilterData().categoryBits == FoxGame.SPIKES_BIT) ? fixA.getUserData()
                    : fixB.getUserData());
        }

        if (enemy != null && foxy.enemiesInContact.contains(enemy)) {
            foxy.enemiesInContact.remove(enemy);
        }
        if (pinchos != null && foxy.pinchosInContact.contains(pinchos)) {
            foxy.pinchosInContact.remove(pinchos);
        }
    }
}
